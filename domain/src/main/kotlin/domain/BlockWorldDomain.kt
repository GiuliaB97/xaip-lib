package domain

import core.* // ktlint-disable no-wildcard-imports
import dsl.domain
import dsl.problem

object BlockWorldDomain {
    /**
     * property axioms: represents an array of [Axiom].
     */
    val axioms by lazy { arrayOf(Axioms.axiom1, Axioms.axiom2) }
    val actions by lazy {
        arrayOf(Actions.pick, Actions.stack, Actions.unstack, Actions.putdown)
    }
    val operators by lazy {
        arrayOf(
            Operators.pickA,
            Operators.pickB,
            Operators.stackAB,
            Operators.stackBA,
            Operators.unstackAB,
            Operators.putdownA,
            Operators.putdownB,
            Operators.unstackBA,
        )
    }
    val variables by lazy {
        arrayOf(Values.X, Values.Y, Values.Z, Values.W)
    }
    val types by lazy {
        arrayOf(Types.blocks, Types.locations, Types.numbers, Types.strings, Types.anything)
    }
    val predicates by lazy {
        arrayOf(Predicates.at, Predicates.on, Predicates.clear, Predicates.armEmpty)
    }
    val objects by lazy {
        arrayOf(
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
    }
    val problems by lazy {
        arrayOf(
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
    }

    object DomainsDSL {
        val blockWorld by lazy {
            domain {
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
    }

    object ProblemsDSL {
        val problemOnAB by lazy {
            problem(DomainsDSL.blockWorld) {
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
    }

    object Actions {
        val pick by lazy {
            Action.of(
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
        }

        val putdown by lazy {
            Action.of(
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
        }
        val stack by lazy {
            Action.of(
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
        }
        val unstack by lazy {
            Action.of(
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
    }

    object Expressions {
        val expessionAtArm by lazy {
            Fluents.atAArm
        }
        val unaryExpressionNotArmEmpty by lazy {
            UnaryExpression.of(Fluents.armEmpty, "not")
        }
        val unaryExpressionNotAFloor by lazy {
            UnaryExpression.of(Fluents.atAFloor, "not")
        }
        val binaryExpression1 by lazy {
            BinaryExpression.of(
                Fluents.atBFloor,
                unaryExpressionNotAFloor,
                "and",
            )
        }
        val binaryExpression2 by lazy {
            BinaryExpression.of(
                Fluents.atBFloor,
                unaryExpressionNotAFloor,
                "or",
            )
        }
    }

    object Axioms {
        val axiom1 by lazy {
            Axiom.of(
                mapOf(
                    Values.Y to Types.blocks,
                    Values.X to Types.blocks,
                ), // variabili che possono apparire nella regola
                Fluents.atXArm, // cosa dice della regola
                Fluents.onXY,
            ) // conseguenze sempre vere della regola sopra
        }
        val axiom2 by lazy {
            Axiom.of(
                mapOf(
                    Values.Y to Types.blocks,
                    Values.X to Types.blocks,
                ), // variabili che possono apparire nella regola
                Fluents.atYFloor, // cosa dice della regola
                Fluents.onXY,
            ) // conseguenze sempre vere della regola sopra
        }
    } // es XY si muovo sempre assieme-> se Xè sul braccio allora Y è sotto a X

    object Domains {
        val blockWorld by lazy {
            Domain.of(
                name = "block_world",
                predicates = setOf(Predicates.at, Predicates.on, Predicates.armEmpty, Predicates.clear),
                actions = setOf(Actions.pick, Actions.stack, Actions.unstack, Actions.putdown),
                types = setOf(Types.blocks, Types.locations, Types.anything, Types.strings),
            )
        }
        val blockWorldAxiomException by lazy {
            Domain.of(
                name = "block_world_axiom_exception",
                predicates = setOf(Predicates.at, Predicates.on, Predicates.armEmpty),
                actions = setOf(Actions.pick, Actions.stack),
                types = setOf(Types.blocks, Types.locations),
                axioms = Axioms.axiom1,
            )
        }
        val blockWorldWithoutIdempotentActions by lazy {
            Domain.of(
                name = "block_world_without_idempotent_actions",
                predicates = setOf(Predicates.at, Predicates.on, Predicates.armEmpty, Predicates.clear),
                actions = setOf(Actions.pick, Actions.stack),
                types = setOf(Types.blocks, Types.locations, Types.anything, Types.strings),
            )
        }
    }

    object Effects {
        val atXFloor by lazy {
            Effect.of(Fluents.atXFloor, true)
        }
        val armEmpty by lazy {
            Effect.of(Fluents.armEmpty, true)
        }
        val onXY by lazy {
            Effect.of(Fluents.onXY)
        }
    }

    object Fluents {
        val atAFloor by lazy {
            Fluent.positive(Predicates.at, Values.a, Values.floor)
        }
        val atBFloor by lazy {
            Fluent.positive(Predicates.at, Values.b, Values.floor)
        }
        val atCFloor by lazy {
            Fluent.positive(Predicates.at, Values.c, Values.floor)
        }
        val atDFloor by lazy { Fluent.positive(Predicates.at, Values.d, Values.floor) }
        val atAArm by lazy {
            Fluent.positive(Predicates.at, Values.a, Values.arm)
        }
        val atBArm by lazy {
            Fluent.positive(Predicates.at, Values.b, Values.arm)
        }
        val atCArm by lazy {
            Fluent.positive(Predicates.at, Values.c, Values.arm)
        }
        val atDArm by lazy {
            Fluent.positive(Predicates.at, Values.d, Values.arm)
        }
        val atXFloor by lazy {
            Fluent.positive(Predicates.at, Values.X, Values.floor)
        }
        val atXArm by lazy {
            Fluent.positive(Predicates.at, Values.X, Values.arm)
        }
        val atYFloor by lazy {
            Fluent.positive(Predicates.at, Values.Y, Values.floor)
        }
        val atYArm by lazy {
            Fluent.positive(Predicates.at, Values.Y, Values.arm)
        }
        val atWFloor by lazy {
            Fluent.positive(Predicates.at, Values.W, Values.floor)
        }
        val atWArm by lazy {
            Fluent.positive(Predicates.at, Values.W, Values.arm)
        }
        val atZFloor by lazy {
            Fluent.positive(Predicates.at, Values.Z, Values.floor)
        }
        val atZArm by lazy {
            Fluent.positive(Predicates.at, Values.Z, Values.arm)
        }

        val armEmpty by lazy {
            Fluent.positive(Predicates.armEmpty)
        }
        val onAB by lazy {
            Fluent.positive(Predicates.on, Values.a, Values.b)
        }
        val onBA by lazy {
            Fluent.positive(Predicates.on, Values.b, Values.a)
        }
        val onAC by lazy {
            Fluent.positive(Predicates.on, Values.a, Values.c)
        }
        val onCA by lazy {
            Fluent.positive(Predicates.on, Values.c, Values.a)
        }
        val onAD by lazy {
            Fluent.positive(Predicates.on, Values.a, Values.d)
        }
        val onDA by lazy {
            Fluent.positive(Predicates.on, Values.d, Values.a)
        }
        val onBC by lazy {
            Fluent.positive(Predicates.on, Values.b, Values.c)
        }
        val onCB by lazy {
            Fluent.positive(Predicates.on, Values.c, Values.b)
        }
        val onBD by lazy {
            Fluent.positive(Predicates.on, Values.b, Values.d)
        }
        val onDB by lazy {
            Fluent.positive(Predicates.on, Values.d, Values.b)
        }
        val onCD by lazy {
            Fluent.positive(Predicates.on, Values.c, Values.d)
        }
        val onDC by lazy {
            Fluent.positive(Predicates.on, Values.d, Values.c)
        }
        val onAX by lazy {
            Fluent.positive(Predicates.on, Values.a, Values.X)
        }
        val onDX by lazy {
            Fluent.positive(Predicates.on, Values.d, Values.X)
        }
        val onXA by lazy {
            Fluent.positive(Predicates.on, Values.X, Values.a)
        }
        val onBX by lazy {
            Fluent.positive(Predicates.on, Values.b, Values.X)
        }

        val clearA by lazy {
            Fluent.positive(Predicates.clear, Values.a)
        }
        val clearB by lazy {
            Fluent.positive(Predicates.clear, Values.b)
        }
        val clearC by lazy {
            Fluent.positive(Predicates.clear, Values.c)
        }
        val clearD by lazy {
            Fluent.positive(Predicates.clear, Values.d)
        }
        val clearX by lazy {
            Fluent.positive(Predicates.clear, Values.X)
        }
        val clearY by lazy {
            Fluent.positive(Predicates.clear, Values.Y)
        }
        val clearZ by lazy {
            Fluent.positive(Predicates.clear, Values.Z)
        }
        val clearW by lazy {
            Fluent.positive(Predicates.clear, Values.Z)
        }
        val onXY by lazy {
            Fluent.positive(Predicates.on, Values.X, Values.Y)
        }
        val onYX by lazy {
            Fluent.positive(Predicates.on, Values.Y, Values.X)
        }
        val onWX by lazy {
            Fluent.positive(Predicates.on, Values.W, Values.X)
        }
        val onWZ by lazy {
            Fluent.positive(Predicates.on, Values.W, Values.Z)
        }
        val onXW by lazy {
            Fluent.positive(Predicates.on, Values.X, Values.W)
        }
        val onZW by lazy {
            Fluent.positive(Predicates.on, Values.Z, Values.W)
        }
    }

    object Goals {
        val atXatYarm by lazy {
            FluentBasedGoal.of(
                Fluents.atYArm,
                Fluents.atXArm,
                Fluents.atXFloor,
            )
        }
        val onZWatXarm by lazy {
            FluentBasedGoal.of(
                Fluents.atXArm,
                Fluents.onZW,
            )
        }
        val onBDCA by lazy {
            FluentBasedGoal.of(
                Fluents.clearB,
                Fluents.onBD,
                Fluents.onDC,
                Fluents.onCA,
                Fluents.atAFloor,
            )
        }
        val onDCAatBarm by lazy {
            FluentBasedGoal.of(
                Fluents.atBArm,
                Fluents.clearD,
                Fluents.onDC,
                Fluents.onCA,
                Fluents.atAFloor,
            )
        }
        val onDCA by lazy {
            FluentBasedGoal.of(Fluents.onDC, Fluents.onCA, Fluents.atAFloor)
        }
        val onDCB by lazy {
            FluentBasedGoal.of(Fluents.onDC, Fluents.onCB, Fluents.atAFloor)
        }
        val onBConAD by lazy {
            FluentBasedGoal.of(Fluents.onBC, Fluents.onAD, Fluents.atDFloor, Fluents.atCFloor)
        }
        val armNotEmpty by lazy {
            FluentBasedGoal.of(Fluents.atXArm)
        }
        val atAfloorAtBfloorAtCfloorAtDfloor by lazy {
            FluentBasedGoal.of(
                Fluents.atAFloor,
                Fluents.atCFloor,
                Fluents.atDFloor,
                Fluents.atBFloor,
            )
        }
        val onDXonXA by lazy {
            FluentBasedGoal.of(Fluents.onDX, Fluents.onXA)
        }
        val onCB by lazy {
            FluentBasedGoal.of(Fluents.atAFloor, Fluents.atBFloor, Fluents.atDFloor, Fluents.onCB)
        }

        // i goal vanno dichiarati in ordine inverso,
        // perché ho un apila quando faccio push per metterli sulllo stack il primo è a offset zero
        val onBC by lazy {
            FluentBasedGoal.of(Fluents.onBC, Fluents.atAFloor, Fluents.atDFloor, Fluents.atCFloor)
        }
        val atXArmAndAtYFloorAndOnWZ by lazy {
            FluentBasedGoal.of(Fluents.atXArm, Fluents.atYFloor, Fluents.onWZ)
        }
        val atCarm by lazy {
            FluentBasedGoal.of(Fluents.atCArm)
        }
        val atBarm by lazy {
            FluentBasedGoal.of(Fluents.atBArm, Fluents.atAFloor)
        }
        val onAatBandBonFloor by lazy {
            FluentBasedGoal.of(Fluents.atBFloor, Fluents.onAB)
        }
        val onAX by lazy {
            FluentBasedGoal.of(Fluents.onAX)
        }
        val onCAonBY by lazy {
            FluentBasedGoal.of(Fluents.onBX, Fluents.onCA)
        }
        val pickX by lazy {
            FluentBasedGoal.of(Fluents.atXArm)
        }
        val pickXfloorY by lazy {
            FluentBasedGoal.of(Fluents.atXArm, Fluents.atYFloor)
        }
        val onXY by lazy {
            FluentBasedGoal.of(Fluents.onXY)
        }
        val onXYatW by lazy {
            FluentBasedGoal.of(Fluents.atWArm, Fluents.onXY) // caso sfigato
        }
        val onABC by lazy { FluentBasedGoal.of(Fluents.onCA, Fluents.onAB) }
    }

    object ObjectSets {
        val all by lazy {
            ObjectSet.of(
                Types.blocks to setOf(Values.a, Values.b, Values.c, Values.d),
                Types.locations to setOf(Values.floor, Values.arm),
                Types.numbers to setOf(Values.one, Values.two, Values.zero),
            )
        }
        val objects by lazy {
            ObjectSet.of(
                Types.blocks to setOf(Values.a, Values.b, Values.c, Values.d),
                Types.locations to setOf(Values.floor, Values.arm),
            )
        }
    }

    object Operators {
        var pickA: Operator
        var pickB: Operator
        var pickC: Operator
        var pickD: Operator

        var stackAB: Operator
        var stackAC: Operator
        var stackAD: Operator
        var stackBA: Operator
        var stackBC: Operator
        var stackBD: Operator
        var stackCB: Operator
        var stackCA: Operator
        var stackCD: Operator

        var stackDB: Operator
        var stackDC: Operator
        var stackDA: Operator

        var unstackAB: Operator
        var unstackAC: Operator
        var unstackAD: Operator
        var unstackBA: Operator
        var unstackBC: Operator
        var unstackBD: Operator
        var unstackCA: Operator
        var unstackCB: Operator
        var unstackCD: Operator
        var unstackDA: Operator
        var unstackDB: Operator
        var unstackDC: Operator

        val putdownA: Operator
        val putdownB: Operator
        val putdownC: Operator
        val putdownD: Operator

        init {
            pickA = Operator.of(Actions.pick).apply(VariableAssignment.of(Values.X, Values.a))
            pickB = Operator.of(Actions.pick).apply(VariableAssignment.of(Values.X, Values.b))
            pickC = Operator.of(Actions.pick).apply(VariableAssignment.of(Values.X, Values.c))
            pickD = Operator.of(Actions.pick).apply(VariableAssignment.of(Values.X, Values.d))

            stackAB = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.a))
            stackAC = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.a))
            stackAD = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.a))
            stackBA = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.b))
            stackBC = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.b))
            stackBD = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.b))
            stackCB = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.c))
            stackCA = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.c))
            stackCD = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.c))
            stackDB = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.d))
            stackDC = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.d))
            stackDA = Operator.of(Actions.stack).apply(VariableAssignment.of(Values.X, Values.d))

            unstackAB = Operator.of(Actions.unstack).apply(VariableAssignment.of(Values.X, Values.a))
            unstackAC = Operator.of(Actions.unstack).apply(VariableAssignment.of(Values.X, Values.a))
            unstackAD = Operator.of(Actions.unstack).apply(VariableAssignment.of(Values.X, Values.a))
            unstackBA = Operator.of(Actions.unstack).apply(VariableAssignment.of(Values.X, Values.b))
            unstackBC = Operator.of(Actions.unstack).apply(VariableAssignment.of(Values.X, Values.b))
            unstackBD = Operator.of(Actions.unstack).apply(VariableAssignment.of(Values.X, Values.b))
            unstackCB = Operator.of(Actions.unstack).apply(VariableAssignment.of(Values.X, Values.c))
            unstackCA = Operator.of(Actions.unstack).apply(VariableAssignment.of(Values.X, Values.c))
            unstackCD = Operator.of(Actions.unstack).apply(VariableAssignment.of(Values.X, Values.c))
            unstackDB = Operator.of(Actions.unstack).apply(VariableAssignment.of(Values.X, Values.d))
            unstackDC = Operator.of(Actions.unstack).apply(VariableAssignment.of(Values.X, Values.d))
            unstackDA = Operator.of(Actions.unstack).apply(VariableAssignment.of(Values.X, Values.d))

            putdownA = Operator.of(Actions.putdown)
                .apply(VariableAssignment.of(Values.X, Values.a))
            putdownB = Operator.of(Actions.putdown)
                .apply(VariableAssignment.of(Values.X, Values.b))
            putdownC = Operator.of(Actions.putdown)
                .apply(VariableAssignment.of(Values.X, Values.c))
            putdownD = Operator.of(Actions.putdown)
                .apply(VariableAssignment.of(Values.X, Values.d))

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
        val emptyPlan by lazy { Plan.of(emptyList()) }
        val basicPlan by lazy { Plan.of(listOf(Operators.pickA, Operators.stackAB)) }
    }

    object Planners {
        val stripsPlanner by lazy {
            Planner.strips()
        }
    }

    object Predicates {
        val at by lazy { Predicate.of("at", Types.blocks, Types.locations) }
        val on by lazy { Predicate.of("on", Types.blocks, Types.blocks) }
        val armEmpty by lazy { Predicate.of("arm_empty") }
        val clear by lazy { Predicate.of("clear", Types.blocks) }
    }

    object Problems {
        val unstackABunstackCDstackBDCA by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "unstackABunstackCDstackBDCA",
                objects = ObjectSets.all,
                initialState = States.onABonCD,
                goal = Goals.onBDCA,
            )
        }
        val unstackABunstackCDstackDCApickB by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "unstackABunstackCDstackDCApickB",
                objects = ObjectSets.all,
                initialState = States.onABonCD,
                goal = Goals.onDCAatBarm,
            )
        }

        val unstackABpickB by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "unstackABpickB",
                objects = ObjectSets.all,
                initialState = States.onAB,
                goal = Goals.atBarm,
            )
        }

        val unstackABunstackCDstackDCA by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "unstackABunstackCDstackDCA",
                objects = ObjectSets.all,
                initialState = States.onABonCD,
                goal = Goals.onDCA,
            )
        }

        val unstackABstackDCB by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "unstackABstackDCB",
                objects = ObjectSets.all,
                initialState = States.onAB,
                goal = Goals.onDCB,
            )
        }

        val unstackABstackBCstackAD by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "unstackABstackBCstackAD",
                objects = ObjectSets.all,
                initialState = States.onAB,
                goal = Goals.onBConAD,
            )
        }

        val unstackABstackBC by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "unstackABstackBC",
                objects = ObjectSets.all,
                initialState = States.onAB,
                goal = Goals.onBC,
            )
        }
        val pickXpickY by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "pickXpickY",
                objects = ObjectSets.all,
                initialState = States.allBlocksAtFloor,
                goal = Goals.atXatYarm,
            )
        }
        val stackZWpickX by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "stackZWpickX",
                objects = ObjectSets.all,
                initialState = States.allBlocksAtFloor,
                goal = Goals.onZWatXarm,
            )
        }
        val stackBAstackDC by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "stackBAstackDC",
                objects = ObjectSets.all,
                initialState = States.allBlocksAtFloor,
                goal = Goals.onBConAD,
            )
        }

        val armNotEmpty by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "armNotEmpty",
                objects = ObjectSets.all,
                initialState = States.allBlocksAtFloor,
                goal = Goals.armNotEmpty,
            )
        }
        val unstackAB by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "unstackAB",
                objects = ObjectSets.all,
                initialState = States.onAB,
                goal = Goals.atAfloorAtBfloorAtCfloorAtDfloor,
            )
        }
        val stackCB by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "stackCB",
                objects = ObjectSets.all,
                initialState = States.onBAonCD,
                goal = Goals.onCB,
            )
        }

        val stackCAstackBY by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "stackCAstackBY",
                objects = ObjectSets.all,
                initialState = States.allBlocksAtFloor,
                goal = Goals.onCAonBY,
            )
        }

        val stackBC by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "stackBC",
                objects = ObjectSets.all,
                initialState = States.onBAonCD,
                goal = Goals.onBC,
            )
        }
        val stackAny by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "stackAny",
                objects = ObjectSets.all,
                initialState = States.allBlocksAtFloor,
                goal = Goals.atXArmAndAtYFloorAndOnWZ,
            )
        }

        val pickC by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "pickC",
                objects = ObjectSets.all,
                initialState = States.allBlocksAtFloor,
                goal = Goals.atCarm,
            )
        }

        val stackAB by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "stackAB",
                objects = ObjectSets.objects,
                initialState = States.allBlocksAtFloor,
                goal = Goals.onAatBandBonFloor,
            )
        }

        val stackAX by lazy {
            Problem.of(
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
        }

        val pickXfloorY by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "pickXfloorY",
                objects = ObjectSets.all,
                initialState = States.allBlocksAtFloor,
                goal = Goals.pickXfloorY,
            )
        }

        val stackXY by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "stackXY",
                objects = ObjectSets.all,
                initialState = States.allBlocksAtFloor,
                goal = Goals.onXY,
            )
        }

        val stackCAB by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "stackCAB",
                objects = ObjectSets.all,
                initialState = States.allBlocksAtFloor,
                goal = Goals.onABC,
            )
        }

        val stackXYpickW by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "stackXYpickW",
                objects = ObjectSets.all,
                initialState = States.allBlocksAtFloor,
                goal = Goals.onXYatW,
            )
        }

        val axiomException by lazy {
            Problem.of(
                domain = Domains.blockWorldAxiomException,
                name = "axiomException",
                objects = ObjectSets.all,
                initialState = States.allBlocksAtFloor,
                goal = Goals.onXYatW,
            )
        }

        val stackDXA by lazy {
            Problem.of(
                domain = Domains.blockWorld,
                name = "stackDXA",
                objects = ObjectSets.all,
                initialState = States.allBlocksAtFloor,
                goal = Goals.onDXonXA,
            )
        }
    }

    object States {
        val onABonCD by lazy {
            State.of(
                Fluents.onAB,
                Fluents.onCD,
                Fluents.clearC,
                Fluents.clearA,
                Fluents.armEmpty,
                Fluents.atBFloor,
                Fluents.atDFloor,
            )
        }

        val onAB by lazy {
            State.of(
                Fluents.onAB,
                Fluents.atCFloor,
                Fluents.atDFloor,
                Fluents.atBFloor,
                Fluents.clearA,
                Fluents.clearC,
                Fluents.clearD,
                Fluents.armEmpty,
            )
        }

        val allBlocksAtFloor by lazy {
            State.of(
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
        }
        val atAArm by lazy {
            State.of(
                Fluents.atAArm,
                Fluents.atBFloor,
                Fluents.atCFloor,
                Fluents.atDFloor,
                Fluents.clearB,
                Fluents.clearC,
                Fluents.clearD,
            )
        }
        val atBArm by lazy {
            State.of(
                Fluents.atAFloor,
                Fluents.atBArm,
                Fluents.atCFloor,
                Fluents.atDFloor,
                Fluents.clearA,
                Fluents.clearC,
                Fluents.clearD,
            )
        }

        val atCArm by lazy {
            State.of(
                Fluents.atAFloor,
                Fluents.atBFloor,
                Fluents.atCArm,
                Fluents.atDFloor,
                Fluents.clearA,
                Fluents.clearB,
                Fluents.clearD,
            )
        }

        val atDArm by lazy {
            State.of(
                Fluents.atAFloor,
                Fluents.atCFloor,
                Fluents.atBFloor,
                Fluents.atDArm,
                Fluents.clearA,
                Fluents.clearB,
                Fluents.clearC,
            )
        }

        val onBAonCD by lazy {
            State.of(
                Fluents.onBA,
                Fluents.onCD,
                Fluents.clearC,
                Fluents.clearB,
                Fluents.armEmpty,
                Fluents.atAFloor,
                Fluents.atDFloor,
            )
        }

        val onBAonDC by lazy {
            State.of(
                Fluents.onBA,
                Fluents.onDC,
                Fluents.clearD,
                Fluents.clearB,
                Fluents.armEmpty,
                Fluents.atAFloor,
                Fluents.atCFloor,
            )
        }

        val onCAatBfloorDfloor by lazy {
            State.of(
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
    }

    object Types {
        val anything by lazy { Type.of("anything") }
        val strings by lazy { Type.of("strings", anything) }
        val numbers by lazy { Type.of("numbers", anything) }
        val blocks by lazy { Type.of("blocks", strings) }
        val locations by lazy { Type.of("locations", strings) }
    }

    object Values {
        val a by lazy { Object.of("a") }
        val b by lazy { Object.of("b") }
        val c by lazy { Object.of("c") }
        val d by lazy { Object.of("d") }

        val floor by lazy { Object.of("floor") }
        val arm by lazy { Object.of("arm") }

        val zero by lazy { Object.of(0) }
        val one by lazy { Object.of(1) }
        val two by lazy { Object.of(2) }

        val W by lazy { Variable.of("W") }
        val X by lazy { Variable.of("X") }
        val Y by lazy { Variable.of("Y") }
        val Z by lazy { Variable.of("Z") }
    }

    object VariableAssignments {
        val y2x by lazy { VariableAssignment.of(Values.Y, Values.X) }
        val x2floor by lazy { VariableAssignment.of(Values.X, Values.floor) }
        val x2arm by lazy { VariableAssignment.of(Values.X, Values.arm) }
        val x2a by lazy { VariableAssignment.of(Values.X, Values.a) }
    }
}
