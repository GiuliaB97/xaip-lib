package domain

import core.* // ktlint-disable no-wildcard-imports
import dsl.domain
import dsl.problem

object BlockWorldDomain {
    /**
     * property axioms: represents an array of [Axiom].
     */
    val axioms = arrayOf(Axioms.axiom1, Axioms.axiom2)
    val actions = arrayOf(Actions.pick, Actions.stack, Actions.unstack, Actions.putdown)
    val operators = arrayOf(
        Operators.pickA,
        Operators.pickB,
        Operators.stackAB,
        Operators.stackBA,
        Operators.unstackAB,
        Operators.putdownA,
        Operators.putdownB,
        Operators.unstackBA,
    )
    val variables = arrayOf(Values.W, Values.X, Values.Y, Values.Z)
    val types = arrayOf(Types.blocks, Types.locations, Types.numbers, Types.strings, Types.anything)
    val predicates = arrayOf(Predicates.at, Predicates.on, Predicates.clear, Predicates.armEmpty)
    val objects = arrayOf(
        Object.of("a"),
        Object.of("b"),
        Object.of("c"),
        Object.of("d"),
        Object.of("floor"),
        Object.of("arm"),
        Object.of(0),
        Object.of(1),
        Object.of(2),
    )
    val problems = arrayOf(
        Problems.armNotEmpty,
        Problems.unstackABunstackCDstackBDCA,
        Problems.unstackABunstackCDstackDCApickB,
        Problems.unstackAB,
        Problems.unstackABpickB,
        Problems.unstackABunstackCDstackDCA,
        Problems.unstackABstackDCB,
        Problems.unstackABstackBCstackAD,
        Problems.unstackABstackBC,
        Problems.stackBAstackDC,
        Problems.stackCAB,
        Problems.stackBC,
        Problems.stackCB,
        Problems.stackAB,
        Problems.pickC,
    )

    object DomainsDSL {
        val blockWorld = domain {
            name = "block_world"
            types {
                +"anything"
                +"strings"("anything")
                +"blocks"("strings")
                +"locations"("strings")
            }
            predicates {
                +"on"("blocks", "blocks")
                +"at"("blocks", "locations")
                +"clear"("blocks")
                +"arm_empty"()
            }
            actions {
                "pick" {
                    parameters {
                        "X" ofType "blocks"
                    }
                    preconditions {
                        +"arm_empty"()
                        +"clear"("X")
                        +"at"("X", "floor")
                    }
                    effects {
                        +"at"("X", "arm")
                        -"arm_empty"
                        -"at"("X", "floor")
                        -"clear"("X")
                    }
                }
                "stack" {
                    parameters {
                        "X" ofType "blocks"
                        "Y" ofType "locations"
                    }
                    preconditions {
                        +"at"("X", "arm")
                        +"clear"("Y")
                    }
                    effects {
                        +"on"("X", "Y")
                        +"clear"("X")
                        +"arm_empty"
                        -"at"("X", "arm")
                        -"clear"("Y")
                    }
                }

                "unstack" {
                    parameters {
                        "X" ofType "blocks"
                        "Y" ofType "locations"
                    }
                    preconditions {
                        +"on"("X", "Y")
                        +"clear"("X")
                        +"arm_empty"
                    }
                    effects {
                        -"on"("X", "Y")
                        -"clear"("X")
                        -"arm_empty"
                        +"at"("X", "arm")
                        +"clear"("Y")
                    }
                }
                "putdown" {
                    parameters {
                        "X" ofType "blocks"
                    }
                    preconditions {
                        +"at"("X", "arm")
                        +"clear"("Y")
                    }
                    effects {
                        -"at"("X", "arm")
                        +"clear"("X")
                        +"arm_empty"
                        +"at"("X", "floor")
                    }
                }
            }
            axioms {
                parameters {
                    "X" ofType "blocks"
                    "Y" ofType "locations"
                    "W" ofType "anything"
                    "Z" ofType "strings"
                }
                context = "on"("a", "b") or "on"("a", "c")
                // precondizioni
                implies = !"on"("b", "c")
                // postcondizioni
            }
        }
    }

    object ProblemsDSL {
        val problemOnAB = problem(DomainsDSL.blockWorld) {
            objects {
                +"blocks"("a", "b", "c", "d")
            }
            initialState {
                +"at"("a", "floor")
                +"at"("b", "floor")
                +"at"("c", "floor")
                +"at"("d", "floor")
                +"arm_empty"()
                +"clear"("a")
                +"clear"("b")
                +"clear"("c")
                +"clear"("d")
            }
            goals {
                +"on"("a", "b")
            }
        }
    }

    object Actions {
        val pick = Action.of(
            name = "pick",
            parameters = mapOf(
                Values.X to Types.blocks,
            ),
            preconditions = setOf(Fluents.armEmpty, Fluents.clearX, Fluents.atXFloor),
            effects = setOf(
                Effect.of(Fluents.atXArm),
                Effect.negative(Fluents.armEmpty),
                Effect.negative(Fluents.clearX),
                Effect.negative(Fluents.atXFloor),
            ),
        )

        val putdown = Action.of(
            name = "putdown",
            parameters = mapOf(
                Values.X to Types.blocks,
            ),
            preconditions = setOf(Fluents.atXArm),
            effects = setOf(
                Effect.negative(Fluents.atXArm),
                Effect.of(Fluents.clearX),
                Effect.of(Fluents.armEmpty),
                Effect.of(Fluents.atXFloor),
            ),
        )
        val stack = Action.of(
            name = "stack",
            parameters = mapOf(
                Values.X to Types.blocks,
                Values.Y to Types.locations,
            ),
            preconditions = setOf(Fluents.atXArm, Fluents.clearY),
            effects = setOf(
                Effect.of(Fluents.onXY),
                Effect.of(Fluents.clearX),
                Effect.of(Fluents.armEmpty),
                Effect.negative(Fluents.atXArm),
                Effect.negative(Fluents.clearY),
            ),
        )

        val unstack = Action.of(
            name = "unstack",
            parameters = mapOf(
                Values.X to Types.blocks,
                Values.Y to Types.locations,
            ),
            preconditions = setOf(
                Fluents.onXY,
                Fluents.clearX,
                Fluents.armEmpty,
            ),
            effects = setOf(
                Effect.negative(Fluents.clearX),
                Effect.negative(Fluents.onXY),
                Effect.negative(Fluents.armEmpty),
                Effect.of(Fluents.atXArm),
                Effect.of(Fluents.clearY),
            ),
        )
    }

    object Expressions {
        val expressionAtArm = Fluents.atAArm
        val unaryExpressionNotArmEmpty = UnaryExpression.of(Fluents.armEmpty, "not")
        val unaryExpressionNotAFloor = UnaryExpression.of(Fluents.atAFloor, "not")
        val binaryExpression1 = BinaryExpression.of(
            Fluents.atBFloor,
            unaryExpressionNotAFloor,
            "and",
        )
        val binaryExpression2 = BinaryExpression.of(
            Fluents.atBFloor,
            unaryExpressionNotAFloor,
            "or",
        )
    }

    object Axioms {
        val axiom1 = Axiom.of(
            mapOf(Values.Y to Types.blocks, Values.X to Types.blocks), // variabili che possono apparire nella regola
            Fluents.atXArm, // cosa dice della regola
            Fluents.onXY,
        ) // conseguenze sempre vere della regola sopra
        val axiom2 = Axiom.of(
            mapOf(Values.Y to Types.blocks, Values.X to Types.blocks), // variabili che possono apparire nella regola
            Fluents.atYFloor, // cosa dice della regola
            Fluents.onXY,
        ) // conseguenze sempre vere della regola sopra
    } // es XY si muovo sempre assieme-> se Xè sul braccio allora Y è sotto a X

    object Domains {
        val blockWorld = Domain.of(
            name = "block_world",
            predicates = setOf(Predicates.at, Predicates.on, Predicates.armEmpty, Predicates.clear),
            actions = setOf(Actions.pick, Actions.stack, Actions.unstack, Actions.putdown),
            types = setOf(Types.blocks, Types.locations, Types.anything, Types.strings),
        )
        val blockWorldAxiomException = Domain.of(
            name = "block_world_axiom_exception",
            predicates = setOf(Predicates.at, Predicates.on, Predicates.armEmpty),
            actions = setOf(Actions.pick, Actions.stack),
            types = setOf(Types.blocks, Types.locations),
            axioms = Axioms.axiom1,
        )

        val blockWorldWithoutIdempotentActions = Domain.of(
            name = "block_world_without_idempotent_actions",
            predicates = setOf(Predicates.at, Predicates.on, Predicates.armEmpty, Predicates.clear),
            actions = setOf(Actions.pick, Actions.stack),
            types = setOf(Types.blocks, Types.locations, Types.anything, Types.strings),
        )
    }

    object Effects {
        val atXFloor = Effect.of(Fluents.atXFloor, true)
        val armEmpty = Effect.of(Fluents.armEmpty, true)
        val onXY = Effect.of(Fluents.onXY)
    }

    object Fluents {
        val atAFloor = Fluent.positive(Predicates.at, Values.a, Values.floor)
        val atBFloor = Fluent.positive(Predicates.at, Values.b, Values.floor)
        val atCFloor = Fluent.positive(Predicates.at, Values.c, Values.floor)
        val atDFloor = Fluent.positive(Predicates.at, Values.d, Values.floor)

        val atAArm = Fluent.positive(Predicates.at, Values.a, Values.arm)
        val atBArm = Fluent.positive(Predicates.at, Values.b, Values.arm)
        val atCArm = Fluent.positive(Predicates.at, Values.c, Values.arm)
        val atDArm = Fluent.positive(Predicates.at, Values.d, Values.arm)

        val atXFloor = Fluent.positive(Predicates.at, Values.X, Values.floor)
        val atXArm = Fluent.positive(Predicates.at, Values.X, Values.arm)

        val atYFloor = Fluent.positive(Predicates.at, Values.Y, Values.floor)
        val atYArm = Fluent.positive(Predicates.at, Values.Y, Values.arm)

        val atWFloor = Fluent.positive(Predicates.at, Values.W, Values.floor)
        val atWArm = Fluent.positive(Predicates.at, Values.W, Values.arm)

        val atZFloor = Fluent.positive(Predicates.at, Values.Z, Values.floor)
        val atZArm = Fluent.positive(Predicates.at, Values.Z, Values.arm)
        val armEmpty = Fluent.positive(Predicates.armEmpty)

        val onAB = Fluent.positive(Predicates.on, Values.a, Values.b)
        val onBA = Fluent.positive(Predicates.on, Values.b, Values.a)
        val onAC = Fluent.positive(Predicates.on, Values.a, Values.c)
        val onCA = Fluent.positive(Predicates.on, Values.c, Values.a)
        val onAD = Fluent.positive(Predicates.on, Values.a, Values.d)
        val onDA = Fluent.positive(Predicates.on, Values.d, Values.a)
        val onBC = Fluent.positive(Predicates.on, Values.b, Values.c)
        val onCB = Fluent.positive(Predicates.on, Values.c, Values.b)
        val onBD = Fluent.positive(Predicates.on, Values.b, Values.d)
        val onDB = Fluent.positive(Predicates.on, Values.d, Values.b)
        val onCD = Fluent.positive(Predicates.on, Values.c, Values.d)
        val onDC = Fluent.positive(Predicates.on, Values.d, Values.c)

        val onAX = Fluent.positive(Predicates.on, Values.a, Values.X)
        val onDX = Fluent.positive(Predicates.on, Values.d, Values.X)
        val onXA = Fluent.positive(Predicates.on, Values.X, Values.a)
        val onBX = Fluent.positive(Predicates.on, Values.b, Values.X)

        val clearA = Fluent.positive(Predicates.clear, Values.a)
        val clearB = Fluent.positive(Predicates.clear, Values.b)
        val clearC = Fluent.positive(Predicates.clear, Values.c)
        val clearD = Fluent.positive(Predicates.clear, Values.d)
        val clearX = Fluent.positive(Predicates.clear, Values.X)
        val clearY = Fluent.positive(Predicates.clear, Values.Y)
        val clearZ = Fluent.positive(Predicates.clear, Values.Z)
        val clearW = Fluent.positive(Predicates.clear, Values.Z)

        val onXY = Fluent.positive(Predicates.on, Values.X, Values.Y)
        val onYX = Fluent.positive(Predicates.on, Values.Y, Values.X)
        val onWX = Fluent.positive(Predicates.on, Values.W, Values.X)
        val onWZ = Fluent.positive(Predicates.on, Values.W, Values.Z)
        val onXW = Fluent.positive(Predicates.on, Values.X, Values.W)
        val onZW = Fluent.positive(Predicates.on, Values.Z, Values.W)
    }

    object Goals {
        val atXatYarm = FluentBasedGoal.of(
            Fluents.atYArm,
            Fluents.atXArm,
            Fluents.atXFloor,
        )
        val onZWatXarm = FluentBasedGoal.of(
            Fluents.atXArm,
            Fluents.onZW,
        )

        val onBDCA = FluentBasedGoal.of(
            Fluents.clearB,
            Fluents.onBD,
            Fluents.onDC,
            Fluents.onCA,
            Fluents.atAFloor,
        )

        val onDCAatBarm = FluentBasedGoal.of(
            Fluents.atBArm,
            Fluents.clearD,
            Fluents.onDC,
            Fluents.onCA,
            Fluents.atAFloor,
        )

        val onDCA = FluentBasedGoal.of(Fluents.onDC, Fluents.onCA, Fluents.atAFloor)
        val onDCB = FluentBasedGoal.of(Fluents.onDC, Fluents.onCB, Fluents.atAFloor)

        val onBConAD = FluentBasedGoal.of(Fluents.onBC, Fluents.onAD, Fluents.atDFloor, Fluents.atCFloor)
        val armNotEmpty = FluentBasedGoal.of(Fluents.atXArm)
        val atAfloorAtBfloorAtCfloorAtDfloor =
            FluentBasedGoal.of(
                Fluents.atAFloor,
                Fluents.atCFloor,
                Fluents.atDFloor,
                Fluents.atBFloor,
            )
        val onDXonXA = FluentBasedGoal.of(Fluents.onDX, Fluents.onXA)
        val onCB = FluentBasedGoal.of(Fluents.atAFloor, Fluents.atBFloor, Fluents.atDFloor, Fluents.onCB)

        // i goal vanno dichiarati in ordine inverso,
        // perché ho un apila quando faccio push per metterli sulllo stack il primo è a offset zero
        val onBC = FluentBasedGoal.of(Fluents.onBC, Fluents.atAFloor, Fluents.atDFloor, Fluents.atCFloor)
        val atXArmAndAtYFloorAndOnWZ = FluentBasedGoal.of(Fluents.atXArm, Fluents.atYFloor, Fluents.onWZ)
        val atCarm = FluentBasedGoal.of(Fluents.atCArm)
        val atBarm = FluentBasedGoal.of(Fluents.atBArm, Fluents.atAFloor)
        val onAatBandBonFloor = FluentBasedGoal.of(Fluents.atBFloor, Fluents.onAB)
        val onAX = FluentBasedGoal.of(Fluents.onAX)
        val onCAonBY = FluentBasedGoal.of(Fluents.onBX, Fluents.onCA)
        val pickX = FluentBasedGoal.of(Fluents.atXArm)
        val pickXfloorY = FluentBasedGoal.of(Fluents.atXArm, Fluents.atYFloor)
        val onXY = FluentBasedGoal.of(Fluents.onXY)
        val onXYatW = FluentBasedGoal.of(Fluents.atWArm, Fluents.onXY) // caso sfigato
        val onABC = FluentBasedGoal.of(Fluents.onCA, Fluents.onAB)
    }

    object ObjectSets {
        val all = ObjectSet.of(
            Types.blocks to setOf(Values.a, Values.b, Values.c, Values.d),
            Types.locations to setOf(Values.floor, Values.arm),
            Types.numbers to setOf(Values.one, Values.two, Values.zero),
        )
        val objects = ObjectSet.of(
            Types.blocks to setOf(Values.a, Values.b, Values.c, Values.d),
            Types.locations to setOf(Values.floor, Values.arm),
        )
    }

    object Operators {
        val pickA = Operator.of(Actions.pick).apply(VariableAssignment.of(Values.X, Values.a))
        val pickB = Operator.of(Actions.pick).apply(VariableAssignment.of(Values.X, Values.b))
        val pickC = Operator.of(Actions.pick).apply(VariableAssignment.of(Values.X, Values.c))
        val pickD = Operator.of(Actions.pick).apply(VariableAssignment.of(Values.X, Values.d))

        var stackAB = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.a))
        var stackAC = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.a))
        var stackAD = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.a))

        var stackBA = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.b))
        var stackBC = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.b))
        var stackBD = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.b))

        var stackCB = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.c))
        var stackCA = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.c))
        var stackCD = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.c))

        var stackDB = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.d))
        var stackDC = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.d))
        var stackDA = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.d))

        var unstackAB = Operator.of(Actions.unstack)
            .apply(VariableAssignment.of(Values.X, Values.a))
        var unstackAC = Operator.of(Actions.unstack)
            .apply(VariableAssignment.of(Values.X, Values.a))
        var unstackAD = Operator.of(Actions.unstack)
            .apply(VariableAssignment.of(Values.X, Values.a))

        var unstackBA = Operator.of(Actions.unstack)
            .apply(VariableAssignment.of(Values.X, Values.b))
        var unstackBC = Operator.of(Actions.unstack)
            .apply(VariableAssignment.of(Values.X, Values.b))
        var unstackBD = Operator.of(Actions.unstack)
            .apply(VariableAssignment.of(Values.X, Values.b))

        var unstackCA = Operator.of(Actions.unstack)
            .apply(VariableAssignment.of(Values.X, Values.c))
        var unstackCB = Operator.of(Actions.unstack)
            .apply(VariableAssignment.of(Values.X, Values.c))
        var unstackCD = Operator.of(Actions.unstack)
            .apply(VariableAssignment.of(Values.X, Values.c))

        var unstackDA = Operator.of(Actions.unstack)
            .apply(VariableAssignment.of(Values.X, Values.d))
        var unstackDB = Operator.of(Actions.unstack)
            .apply(VariableAssignment.of(Values.X, Values.d))
        var unstackDC = Operator.of(Actions.unstack)
            .apply(VariableAssignment.of(Values.X, Values.d))

        val putdownA = Operator.of(Actions.putdown)
            .apply(VariableAssignment.of(Values.X, Values.a))
        val putdownB = Operator.of(Actions.putdown)
            .apply(VariableAssignment.of(Values.X, Values.b))
        val putdownC = Operator.of(Actions.putdown)
            .apply(VariableAssignment.of(Values.X, Values.c))
        val putdownD = Operator.of(Actions.putdown)
            .apply(VariableAssignment.of(Values.X, Values.d))

        init {
            stackAB = stackAB.apply(VariableAssignment.of(Values.Y, Values.b))
            stackAC = stackAC.apply(VariableAssignment.of(Values.Y, Values.c))
            stackAD = stackAD.apply(VariableAssignment.of(Values.Y, Values.d))

            stackBA = stackBA.apply(VariableAssignment.of(Values.Y, Values.a))
            stackBC = stackBC.apply(VariableAssignment.of(Values.Y, Values.c))
            stackBD = stackBD.apply(VariableAssignment.of(Values.Y, Values.d))

            stackCB = stackCB.apply(VariableAssignment.of(Values.Y, Values.b))
            stackCA = stackCA.apply(VariableAssignment.of(Values.Y, Values.a))
            stackCD = stackCD.apply(VariableAssignment.of(Values.Y, Values.d))

            stackDA = stackDA.apply(VariableAssignment.of(Values.Y, Values.a))
            stackDB = stackDB.apply(VariableAssignment.of(Values.Y, Values.b))
            stackDC = stackDC.apply(VariableAssignment.of(Values.Y, Values.c))

            unstackAB = unstackAB.apply(VariableAssignment.of(Values.Y, Values.b))
            unstackAC = unstackAC.apply(VariableAssignment.of(Values.Y, Values.c))
            unstackAD = unstackAD.apply(VariableAssignment.of(Values.Y, Values.d))

            unstackBA = unstackBA.apply(VariableAssignment.of(Values.Y, Values.a))
            unstackBC = unstackBC.apply(VariableAssignment.of(Values.Y, Values.c))
            unstackBD = unstackBD.apply(VariableAssignment.of(Values.Y, Values.d))

            unstackCA = unstackCA.apply(VariableAssignment.of(Values.Y, Values.a))
            unstackCB = unstackCB.apply(VariableAssignment.of(Values.Y, Values.b))
            unstackCD = unstackCD.apply(VariableAssignment.of(Values.Y, Values.d))

            unstackDA = unstackDA.apply(VariableAssignment.of(Values.Y, Values.a))
            unstackDB = unstackDB.apply(VariableAssignment.of(Values.Y, Values.b))
            unstackDC = unstackDC.apply(VariableAssignment.of(Values.Y, Values.c))
        }
    }

    object Plans {
        val emptyPlan = Plan.of(emptyList())
        val basicPlan = Plan.of(listOf(Operators.pickA, Operators.stackAB))
    }

    object Planners {
        val stripsPlanner = Planner.strips()
    }

    object Predicates {
        val at = Predicate.of("at", Types.blocks, Types.locations)
        val on = Predicate.of("on", Types.blocks, Types.blocks)
        val armEmpty = Predicate.of("arm_empty")
        val clear = Predicate.of("clear", Types.blocks)
    }

    object Problems {
        val unstackABunstackCDstackBDCA = Problem.of(
            domain = Domains.blockWorld,
            name = "unstackABunstackCDstackBDCA",
            objects = ObjectSets.all,
            initialState = States.onABonCD,
            goal = Goals.onBDCA,
        )

        val unstackABunstackCDstackDCApickB = Problem.of(
            domain = Domains.blockWorld,
            name = "unstackABunstackCDstackDCApickB",
            objects = ObjectSets.all,
            initialState = States.onABonCD,
            goal = Goals.onDCAatBarm,
        )

        val unstackABpickB = Problem.of(
            domain = Domains.blockWorld,
            name = "unstackABpickB",
            objects = ObjectSets.all,
            initialState = States.onAB,
            goal = Goals.atBarm,
        )

        val unstackABunstackCDstackDCA = Problem.of(
            domain = Domains.blockWorld,
            name = "unstackABunstackCDstackDCA",
            objects = ObjectSets.all,
            initialState = States.onABonCD,
            goal = Goals.onDCA,
        )

        val unstackABstackDCB = Problem.of(
            domain = Domains.blockWorld,
            name = "unstackABstackDCB",
            objects = ObjectSets.all,
            initialState = States.onAB,
            goal = Goals.onDCB,
        )

        val unstackABstackBCstackAD = Problem.of(
            domain = Domains.blockWorld,
            name = "unstackABstackBCstackAD",
            objects = ObjectSets.all,
            initialState = States.onAB,
            goal = Goals.onBConAD,
        )

        val unstackABstackBC = Problem.of(
            domain = Domains.blockWorld,
            name = "unstackABstackBC",
            objects = ObjectSets.all,
            initialState = States.onAB,
            goal = Goals.onBC,
        )
        val pickXpickY = Problem.of(
            domain = Domains.blockWorld,
            name = "pickXpickY",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.atXatYarm,
        )
        val stackZWpickX = Problem.of(
            domain = Domains.blockWorld,
            name = "stackZWpickX",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.onZWatXarm,
        )

        val stackBAstackDC = Problem.of(
            domain = Domains.blockWorld,
            name = "stackBAstackDC",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.onBConAD,
        )

        val armNotEmpty = Problem.of(
            domain = Domains.blockWorld,
            name = "armNotEmpty",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.armNotEmpty,
        )
        val unstackAB = Problem.of(
            domain = Domains.blockWorld,
            name = "unstackAB",
            objects = ObjectSets.all,
            initialState = States.onAB,
            goal = Goals.atAfloorAtBfloorAtCfloorAtDfloor,
        )

        val stackCB = Problem.of(
            domain = Domains.blockWorld,
            name = "stackCB",
            objects = ObjectSets.all,
            initialState = States.onBAonCD,
            goal = Goals.onCB,
        )

        val stackCAstackBY = Problem.of(
            domain = Domains.blockWorld,
            name = "stackCAstackBY",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.onCAonBY,
        )

        val stackBC = Problem.of(
            domain = Domains.blockWorld,
            name = "stackBC",
            objects = ObjectSets.all,
            initialState = States.onBAonCD,
            goal = Goals.onBC,
        )

        val stackAny = Problem.of(
            domain = Domains.blockWorld,
            name = "stackAny",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.atXArmAndAtYFloorAndOnWZ,
        )

        val pickC = Problem.of(
            domain = Domains.blockWorld,
            name = "pickC",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.atCarm,
        )

        val stackAB = Problem.of(
            domain = Domains.blockWorld,
            name = "stackAB",
            objects = ObjectSets.objects,
            initialState = States.allBlocksAtFloor,
            goal = Goals.onAatBandBonFloor,
        )

        val stackAX = Problem.of(
            domain = Domains.blockWorld,
            name = "stackAX",
            objects = ObjectSets.objects,
            initialState = States.allBlocksAtFloor,
            goal = Goals.onAX,
        )

        val pickX = Problem.of(
            domain = Domains.blockWorld,
            name = "pickX",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.pickX,
        )

        val pickXfloorY = Problem.of(
            domain = Domains.blockWorld,
            name = "pickXfloorY",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.pickXfloorY,
        )

        val stackXY = Problem.of(
            domain = Domains.blockWorld,
            name = "stackXY",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.onXY,
        )

        val stackCAB = Problem.of(
            domain = Domains.blockWorld,
            name = "stackCAB",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.onABC,
        )

        val stackXYpickW = Problem.of(
            domain = Domains.blockWorld,
            name = "stackXYpickW",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.onXYatW,
        )

        val axiomException = Problem.of(
            domain = Domains.blockWorldAxiomException,
            name = "axiomException",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.onXYatW,
        )

        val stackDXA = Problem.of(
            domain = Domains.blockWorld,
            name = "stackDXA",
            objects = ObjectSets.all,
            initialState = States.allBlocksAtFloor,
            goal = Goals.onDXonXA,
        )
    }

    object States {
        val onABonCD = State.of(
            Fluents.onAB,
            Fluents.onCD,
            Fluents.clearC,
            Fluents.clearA,
            Fluents.armEmpty,
            Fluents.atBFloor,
            Fluents.atDFloor,
        )

        val onAB = State.of(
            Fluents.onAB,
            Fluents.atCFloor,
            Fluents.atDFloor,
            Fluents.atBFloor,
            Fluents.clearA,
            Fluents.clearC,
            Fluents.clearD,
            Fluents.armEmpty,
        )

        val allBlocksAtFloor = State.of(
            Fluents.atAFloor,
            Fluents.atBFloor,
            Fluents.atCFloor,
            Fluents.atDFloor,
            Fluents.armEmpty,
            Fluents.clearA,
            Fluents.clearB,
            Fluents.clearC,
            Fluents.clearD,
        )

        val atAArm = State.of(
            Fluents.atAArm,
            Fluents.atBFloor,
            Fluents.atCFloor,
            Fluents.atDFloor,
            Fluents.clearB,
            Fluents.clearC,
            Fluents.clearD,
        )

        val atBArm = State.of(
            Fluents.atAFloor,
            Fluents.atBArm,
            Fluents.atCFloor,
            Fluents.atDFloor,
            Fluents.clearA,
            Fluents.clearC,
            Fluents.clearD,
        )

        val atCArm = State.of(
            Fluents.atAFloor,
            Fluents.atBFloor,
            Fluents.atCArm,
            Fluents.atDFloor,
            Fluents.clearA,
            Fluents.clearB,
            Fluents.clearD,
        )

        val atDArm = State.of(
            Fluents.atAFloor,
            Fluents.atCFloor,
            Fluents.atBFloor,
            Fluents.atDArm,
            Fluents.clearA,
            Fluents.clearB,
            Fluents.clearC,
        )

        val onBAonCD = State.of(
            Fluents.onBA,
            Fluents.onCD,
            Fluents.clearC,
            Fluents.clearB,
            Fluents.armEmpty,
            Fluents.atAFloor,
            Fluents.atDFloor,
        )

        val onBAonDC = State.of(
            Fluents.onBA,
            Fluents.onDC,
            Fluents.clearD,
            Fluents.clearB,
            Fluents.armEmpty,
            Fluents.atAFloor,
            Fluents.atCFloor,
        )

        val onCAatBfloorDfloor = State.of(
            Fluents.clearC,
            Fluents.clearD,
            Fluents.clearB,
            Fluents.atDFloor,
            Fluents.atBFloor,
            Fluents.atAFloor,
            Fluents.onCA,
            Fluents.armEmpty,
        )
    }

    object Types {
        val anything = Type.of("anything")
        val strings = Type.of("strings", anything)
        val numbers = Type.of("numbers", anything)
        val blocks = Type.of("blocks", strings)
        val locations = Type.of("locations", strings)
    }

    object Values {
        val a = Object.of("a")
        val b = Object.of("b")
        val c = Object.of("c")
        val d = Object.of("d")

        val floor = Object.of("floor")
        val arm = Object.of("arm")

        val zero = Object.of(0)
        val one = Object.of(1)
        val two = Object.of(2)

        val W = Variable.of("W")
        val X = Variable.of("X")
        val Y = Variable.of("Y")
        val Z = Variable.of("Z")
    }

    object VariableAssignments {
        val y2x = VariableAssignment.of(Values.Y, Values.X)
        val x2floor = VariableAssignment.of(Values.X, Values.floor)
        val x2arm = VariableAssignment.of(Values.X, Values.arm)
        val x2a = VariableAssignment.of(Values.X, Values.a)
    }
}
