package domain

import core.* // ktlint-disable no-wildcard-imports
import dsl.domain
import dsl.problem

object LogisticsDomain {
    val fluents = setOf(
        Fluents.connectedL1L2,
        Fluents.connectedL1L3,
        Fluents.connectedL2L4,
        Fluents.connectedL3L4,
        Fluents.connectedL4L5,
        Fluents.connectedL2L6,
        Fluents.connectedL5L6,
        Fluents.connectedL5L7,
        Fluents.connectedL1L5,
        Fluents.connectedL2L1,
        Fluents.connectedL3L1,
        Fluents.connectedL4L2,
        Fluents.connectedL4L3,
        Fluents.connectedL5L4,
        Fluents.connectedL6L2,
        Fluents.connectedL6L5,
        Fluents.connectedL7L5,
        Fluents.connectedL5L1,
    )

    object DomainsDSL {
        val logistics = domain {
            name = "logistics"
            types {
                +"anything"
                +"strings"("anything")
                +"locations"("strings")
                +"robots"("strings")
                +"containers"("strings")
            }
            predicates {
                +"connected"("locations", "locations")
                +"atLocation"("robots", "locations")
                +"loaded"("robots", "containers")
                +"unloaded"("robots")
                +"inContainerLocation"("containers", "robots")
            }
            actions {
                "move" {
                    parameters {
                        "X" ofType "robots"
                        "Y" ofType "locations"
                        "Z" ofType "locations"
                    }
                    preconditions {
                        +"connected"("Y", "Z")
                        +"atLocation"("X", "Y")
                    }
                    effects {
                        +"atLocation"("X", "Z")
                        -"atLocation"("X", "Y")
                    }
                }
                "load" {
                    parameters {
                        "Z" ofType "locations"
                        "Y" ofType "containers"
                        "X" ofType "robots"
                    }
                    preconditions {
                        +"atLocation"("X", "Z")
                        +"inContainerLocation"("Y", "Z")
                    }
                    effects {
                        +"loaded"("X", "Y")
                        -"inContainerLocation"("Y", "Z")
                    }
                }
                "unload" {
                    parameters {
                        "Z" ofType "locations"
                        "Y" ofType "containers"
                        "X" ofType "robots"
                    }
                    preconditions {
                        +"atLocation"("X", "Z")
                        +"loaded"("X", "Y")
                    }
                    effects {
                        +"inContainerLocation"("Y", "Z")
                        -"loaded"("X", "Y")
                    }
                }
            }
        }
    }

    object ProblemsDSL {
        val rToX = problem(DomainsDSL.logistics) {
            name = "rToX"
            objects {
                +"robots"("r")
                +"locations"("l1", "l2", "l3", "l4", "l5", "l6", "l7")
                +"containers"("c1", "c2")
            }
            initialState {
                +"atLocation"("r", "l1")
                +"inContainerLocation"("c1", "l2")
                +"inContainerLocation"("c2", "l3")
                +"connected"("l1", "l2")
                +"connected"("l1", "l3")
                +"connected"("l2", "l4")
                +"connected"("l3", "l4")
                +"connected"("l4", "l5")
                +"connected"("l1", "l6")
                +"connected"("l5", "l6")
                +"connected"("l5", "l7")
                +"connected"("l1", "l5")
                +"connected"("l2", "l1")
                +"connected"("l3", "l1")
                +"connected"("l4", "l2")
                +"connected"("l4", "l3")
                +"connected"("l5", "l4")
                +"connected"("l6", "l2")
                +"connected"("l6", "l5")
                +"connected"("l7", "l5")
                +"connected"("l5", "l1")
            }
            goals {
                +"atLocation"("r", "Y")
            }
        }

        val robotFromLoc1ToLoc5Container1FromLoc2ToLoc4Container2FromLoc3ToLoc1 = problem(DomainsDSL.logistics) {
            name = "robotFromLoc1ToLoc5Container1FromLoc2ToLoc4Container2FromLoc3ToLoc1"
            objects {
                +"robots"("r")
                +"locations"("l1", "l2", "l3", "l4", "l5", "l6", "l7")
                +"containers"("c1", "c2")
            }
            initialState {
                +"atLocation"("r", "l1")
                +"inContainerLocation"("c1", "l2")
                +"inContainerLocation"("c2", "l3")
                +"connected"("l1", "l2")
                +"connected"("l1", "l3")
                +"connected"("l2", "l4")
                +"connected"("l3", "l4")
                +"connected"("l4", "l5")
                +"connected"("l1", "l6")
                +"connected"("l5", "l6")
                +"connected"("l5", "l7")
                +"connected"("l1", "l5")
                +"connected"("l2", "l1")
                +"connected"("l3", "l1")
                +"connected"("l4", "l2")
                +"connected"("l4", "l3")
                +"connected"("l5", "l4")
                +"connected"("l6", "l2")
                +"connected"("l6", "l5")
                +"connected"("l7", "l5")
                +"connected"("l5", "l1")
            }
            goals {
                +"atLocation"("r", "l5")
                +"inContainerLocation"("c1", "l4")
                +"inContainerLocation"("c2", "l1")
            }
        }

        val robotFromLoc1ToLoc5Container1FromLoc2ToLoc4Container2FromLoc3ToLoc1notDSL =
            problem(Domains.logisticsWorld) {
                name = "robotFromLoc1ToLoc5Container1FromLoc2ToLoc4Container2FromLoc3ToLoc1notDSL"
                objects {
                    +"robots"("r")
                    +"locations"("l1", "l2", "l3", "l4", "l5", "l6", "l7")
                    +"containers"("c1", "c2")
                }
                initialState {
                    +"atLocation"("r", "l1")
                    +"inContainerLocation"("c1", "l2")
                    +"inContainerLocation"("c2", "l3")
                    +"connected"("l1", "l2")
                    +"connected"("l1", "l3")
                    +"connected"("l2", "l4")
                    +"connected"("l3", "l4")
                    +"connected"("l4", "l5")
                    +"connected"("l1", "l6")
                    +"connected"("l5", "l6")
                    +"connected"("l5", "l7")
                    +"connected"("l1", "l5")
                    +"connected"("l2", "l1")
                    +"connected"("l3", "l1")
                    +"connected"("l4", "l2")
                    +"connected"("l4", "l3")
                    +"connected"("l5", "l4")
                    +"connected"("l6", "l2")
                    +"connected"("l6", "l5")
                    +"connected"("l7", "l5")
                    +"connected"("l5", "l1")
                }
                goals {
                    +"atLocation"("r", "l5")
                    +"inContainerLocation"("c1", "l4")
                    +"inContainerLocation"("c2", "l1")
                }
            }
    }

    object Actions {
        val move = Action.of(
            name = "move",
            parameters = mapOf(
                Values.X to Types.robots,
                Values.Y to Types.locations,
                Values.Z to Types.locations,
            ),
            preconditions = setOf(
                Fluents.connectedYZ,
                Fluents.atRobotXlocationY,
            ),
            effects = setOf(
                Effect.of(Fluents.atRobotXlocationZ),
                Effect.negative(Fluents.atRobotXlocationY),
            ),
        )
        val load = Action.of(
            name = "load",
            parameters = mapOf(
                Values.Z to Types.locations,
                Values.Y to Types.containers,
                Values.X to Types.robots,
            ),
            preconditions = setOf(
                Fluents.atRobotXlocationZ,
                Fluents.inContainerYlocationZ,
            ),
            effects = setOf(
                Effect.of(Fluents.loadedXY),
                Effect.negative(Fluents.inContainerYlocationZ),
            ),
        )
        val unload = Action.of(
            name = "unload",
            parameters = mapOf(
                Values.Z to Types.locations,
                Values.Y to Types.containers,
                Values.X to Types.robots,
            ),
            preconditions = setOf(
                Fluents.atRobotXlocationZ,
                Fluents.loadedXY,
            ),
            effects = setOf(
                Effect.of(Fluents.inContainerYlocationZ),
                Effect.negative(Fluents.loadedXY),
            ),
        )
    }

    object Domains {
        val logisticsWorld = Domain.of(
            name = "logistics",
            predicates = setOf(
                Predicates.connected,
                Predicates.atLocation,
                Predicates.loaded,
                Predicates.unloaded,
                Predicates.inContainerLocation,
            ),
            actions = setOf(
                Actions.move,
                Actions.load,
                Actions.unload,
            ),
            types = setOf(
                Types.anything,
                Types.strings,
                Types.locations,
                Types.robots,
                Types.containers,
            ),
        )
    }

    object Fluents {
        val atRlocationY = Fluent.positive(Predicates.atLocation, Values.r, Values.Y)
        val atRobotlocation1 = Fluent.positive(Predicates.atLocation, Values.r, Values.l1)
        val atRobotlocation2 = Fluent.positive(Predicates.atLocation, Values.r, Values.l2)
        val atRobotlocation3 = Fluent.positive(Predicates.atLocation, Values.r, Values.l3)
        val atRobotlocation4 = Fluent.positive(Predicates.atLocation, Values.r, Values.l4)
        val atRobotlocation5 = Fluent.positive(Predicates.atLocation, Values.r, Values.l5)
        val atRobotlocation6 = Fluent.positive(Predicates.atLocation, Values.r, Values.l6)
        val atRobotlocation7 = Fluent.positive(Predicates.atLocation, Values.r, Values.l7)

        val atRobotXlocationY = Fluent.positive(Predicates.atLocation, Values.X, Values.Y)
        val atRobotXlocationZ = Fluent.positive(Predicates.atLocation, Values.X, Values.Z)
        val atRobotXlocationW = Fluent.positive(Predicates.atLocation, Values.X, Values.W)

        val connectedL1L2 = Fluent.positive(Predicates.connected, Values.l1, Values.l2)
        val connectedL1L3 = Fluent.positive(Predicates.connected, Values.l1, Values.l3)
        val connectedL1L5 = Fluent.positive(Predicates.connected, Values.l1, Values.l5)

        val connectedL2L1 = Fluent.positive(Predicates.connected, Values.l2, Values.l1)
        val connectedL2L4 = Fluent.positive(Predicates.connected, Values.l2, Values.l4)
        val connectedL2L6 = Fluent.positive(Predicates.connected, Values.l2, Values.l6)

        val connectedL3L1 = Fluent.positive(Predicates.connected, Values.l3, Values.l1)
        val connectedL3L4 = Fluent.positive(Predicates.connected, Values.l3, Values.l4)

        val connectedL4L2 = Fluent.positive(Predicates.connected, Values.l4, Values.l2)
        val connectedL4L3 = Fluent.positive(Predicates.connected, Values.l4, Values.l3)
        val connectedL4L5 = Fluent.positive(Predicates.connected, Values.l4, Values.l5)

        val connectedL5L1 = Fluent.positive(Predicates.connected, Values.l5, Values.l1)
        val connectedL5L4 = Fluent.positive(Predicates.connected, Values.l5, Values.l4)
        val connectedL5L6 = Fluent.positive(Predicates.connected, Values.l5, Values.l6)
        val connectedL5L7 = Fluent.positive(Predicates.connected, Values.l5, Values.l7)

        val connectedL6L2 = Fluent.positive(Predicates.connected, Values.l6, Values.l2)
        val connectedL6L5 = Fluent.positive(Predicates.connected, Values.l6, Values.l5)
        val connectedL7L5 = Fluent.positive(Predicates.connected, Values.l7, Values.l5)

        val connectedL4L6 = Fluent.positive(Predicates.connected, Values.l4, Values.l6)
        val connectedL6L4 = Fluent.positive(Predicates.connected, Values.l6, Values.l4)

        val connectedXY = Fluent.positive(Predicates.connected, Values.X, Values.Y)
        val connectedXZ = Fluent.positive(Predicates.connected, Values.X, Values.Z)
        val connectedXW = Fluent.positive(Predicates.connected, Values.X, Values.W)
        val connectedYW = Fluent.positive(Predicates.connected, Values.Y, Values.W)
        val connectedYZ = Fluent.positive(Predicates.connected, Values.Y, Values.Z)
        val connectedZW = Fluent.positive(Predicates.connected, Values.Z, Values.W)

        val loadedXY = Fluent.positive(Predicates.loaded, Values.X, Values.Y)

        val unloadedX = Fluent.positive(Predicates.unloaded, Values.X)
        val unloadedY = Fluent.positive(Predicates.unloaded, Values.Y)
        val unloadedZ = Fluent.positive(Predicates.unloaded, Values.Z)
        val unloadedW = Fluent.positive(Predicates.unloaded, Values.W)

        val inContainer1location1 = Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l1)
        val inContainer1location2 = Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l2)
        val inContainer1location3 = Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l3)
        val inContainer1location4 = Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l4)
        val inContainer1location5 = Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l5)
        val inContainer1location6 = Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l6)
        val inContainer1location7 = Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l7)

        val inContainer2location1 = Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l1)
        val inContainer2location2 = Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l2)
        val inContainer2location3 = Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l3)
        val inContainer2location4 = Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l4)
        val inContainer2location5 = Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l5)
        val inContainer2location6 = Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l6)
        val inContainer2location7 = Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l7)

        val inContainerXlocationY = Fluent.positive(Predicates.inContainerLocation, Values.X, Values.Y)
        val inContainerXlocationZ = Fluent.positive(Predicates.inContainerLocation, Values.X, Values.Z)
        val inContainerXlocationW = Fluent.positive(Predicates.inContainerLocation, Values.X, Values.W)
        val inContainerYlocationX = Fluent.positive(Predicates.inContainerLocation, Values.Y, Values.X)
        val inContainerYlocationZ = Fluent.positive(Predicates.inContainerLocation, Values.Y, Values.Z)
        val inContainerYlocationW = Fluent.positive(Predicates.inContainerLocation, Values.Y, Values.W)
    }

    object Goals {
        val atRobotAtlocation5atC1loc4 = FluentBasedGoal.of(Fluents.atRobotlocation5, Fluents.inContainer1location4)
        val atRobotAtlocation2 = FluentBasedGoal.of(Fluents.atRobotlocation2)
        val atRobotAtLocationY = FluentBasedGoal.of(Fluents.atRlocationY)
        val atRobotAtlocation3 = FluentBasedGoal.of(Fluents.atRobotlocation3)
        val inContainerLocation4 = FluentBasedGoal.of(
            Fluents.inContainer1location4,
        )
        val atRobotAtlocation3InContainer1Location4 = FluentBasedGoal.of(
            Fluents.atRobotlocation4,
            Fluents.inContainer1location4,
        )

        val atRobotAtlocation5inContainer1Location4InContainer2Location1 = FluentBasedGoal.of(
            Fluents.atRobotlocation5,
            Fluents.inContainer1location4,
            Fluents.inContainer2location1,
        )
    }

    object ObjectSets {
        val all = ObjectSet.of(
            Types.robots to setOf(Values.r),
            Types.locations to setOf(Values.l1, Values.l2, Values.l3, Values.l4, Values.l5, Values.l6, Values.l7),
            Types.containers to setOf(Values.c1, Values.c2),
        )
    }

    object Problems {

        val rToXdslDomain = Problem.of(
            domain = DomainsDSL.logistics,
            name = "rToXdslDomain",
            objects = ObjectSets.all,
            initialState = States.initial,
            goal = Goals.atRobotAtlocation5inContainer1Location4InContainer2Location1,
        )

        val rToX = Problem.of(
            domain = Domains.logisticsWorld,
            name = "rToX",
            objects = ObjectSets.all,
            initialState = States.initial,
            goal = Goals.atRobotAtLocationY,
        )

        val robotFromLoc1ToLoc2 = Problem.of(
            domain = Domains.logisticsWorld,
            name = "robotFromLoc1ToLoc2",
            objects = ObjectSets.all,
            initialState = States.initial,
            goal = Goals.atRobotAtlocation2,
        )

        val robotFromLoc1ToLoc5C1fromLoc2toLoc4 = Problem.of(
            domain = Domains.logisticsWorld,
            name = "robotFromLoc1ToLoc5C1fromLoc2toLoc4",
            objects = ObjectSets.all,
            initialState = States.initial,
            goal = Goals.atRobotAtlocation5atC1loc4,
        )

        val inContainerLocation4 = Problem.of(
            domain = Domains.logisticsWorld,
            name = "inContainerLocation4",
            objects = ObjectSets.all,
            initialState = States.initial,
            goal = Goals.inContainerLocation4,
        )

        val robotFromLoc1ToLoc2ContainerFromLocation2ToLocation4 = Problem.of(
            domain = Domains.logisticsWorld,
            name = "robotFromLoc1ToLoc2ContainerFromLocation2ToLocation4",
            objects = ObjectSets.all,
            initialState = States.initial,
            goal = Goals.atRobotAtlocation3InContainer1Location4,
        )

        val robotFromLoc1ToLoc5Container1FromLoc2ToLoc4Container2FromLoc3ToLoc1 = Problem.of(
            domain = Domains.logisticsWorld,
            name = "robotFromLoc1ToLoc5Container1FromLoc2ToLoc4Container2FromLoc3ToLoc1",
            objects = ObjectSets.all,
            initialState = States.initial,
            goal = Goals.atRobotAtlocation5inContainer1Location4InContainer2Location1,
        )

        val basicRobotFromLocation1ToLocation2 = Problem.of(
            domain = Domains.logisticsWorld,
            name = "basicRobotFromLocation1ToLocation2",
            objects = ObjectSets.all,
            initialState = States.robotInLoc1,
            goal = Goals.atRobotAtlocation2,
        )
    }

    object Predicates {
        val connected = Predicate.of("connected", Types.locations, Types.locations)
        val atLocation = Predicate.of("atLocation", Types.robots, Types.locations)
        val loaded = Predicate.of("loaded", Types.robots, Types.containers)
        val unloaded = Predicate.of("unloaded", Types.robots)
        val inContainerLocation = Predicate.of("inContainerLocation", Types.containers, Types.robots)
    }

    object States {
        val robotInLoc1 = State.of(
            mutableSetOf(Fluents.atRobotlocation1).also { it.addAll(fluents) },
        )

        val initial = State.of(
            Fluents.atRobotlocation1,
            Fluents.inContainer1location2,
            Fluents.inContainer2location3,
            Fluents.connectedL1L2,
            Fluents.connectedL1L3,
            Fluents.connectedL2L4,
            Fluents.connectedL3L4,
            Fluents.connectedL4L5,
            Fluents.connectedL2L6,
            Fluents.connectedL5L6,
            Fluents.connectedL5L7,
            Fluents.connectedL1L5,
            Fluents.connectedL2L1,
            Fluents.connectedL3L1,
            Fluents.connectedL4L2,
            Fluents.connectedL4L3,
            Fluents.connectedL5L4,
            Fluents.connectedL6L2,
            Fluents.connectedL6L5,
            Fluents.connectedL7L5,
            Fluents.connectedL5L1,
        )

        val alternativeInitialState = State.of(
            Fluents.connectedL1L2,
            Fluents.connectedL1L3,
            Fluents.connectedL2L4,
            Fluents.connectedL3L4,
            Fluents.connectedL4L5,
            Fluents.connectedL2L6,
            Fluents.connectedL5L6,
            Fluents.connectedL5L7,
            Fluents.connectedL1L5,
            Fluents.connectedL2L1,
            Fluents.connectedL3L1,
            Fluents.connectedL4L2,
            Fluents.connectedL4L3,
            Fluents.connectedL5L4,
            Fluents.connectedL6L2,
            Fluents.connectedL6L5,
            Fluents.connectedL7L5,
            Fluents.connectedL5L1,
            Fluents.connectedL6L4,
            Fluents.connectedL4L6,
        )

        val alternativeState = State.of(
            Fluents.connectedL1L2,
            Fluents.connectedL1L3,
            Fluents.connectedL2L4,
            Fluents.connectedL3L4,
            Fluents.connectedL4L5,
            Fluents.connectedL2L6,
            Fluents.connectedL5L6,
            Fluents.connectedL5L7,
            Fluents.connectedL1L5,
            Fluents.connectedL2L1,
            Fluents.connectedL3L1,
            Fluents.connectedL4L2,
            Fluents.connectedL4L3,
            Fluents.connectedL5L4,
            Fluents.connectedL6L2,
            Fluents.connectedL6L5,
            Fluents.connectedL7L5,
            Fluents.connectedL5L1,
            Fluents.connectedL6L4,
            Fluents.connectedL4L6,
            Fluents.atRobotlocation4,
            Fluents.inContainer1location4,
            Fluents.inContainer2location1,
        )
    }

    object Types {
        val anything = Type.of("anything")
        val strings = Type.of("strings", anything)
        val locations = Type.of("locations", strings)
        val robots = Type.of("robots", strings)
        val containers = Type.of("containers", strings)
    }

    object Values {
        val r = Object.of("r")

        val c1 = Object.of("c1")
        val c2 = Object.of("c2")

        val l1 = Object.of("l1")
        val l2 = Object.of("l2")
        val l3 = Object.of("l3")
        val l4 = Object.of("l4")
        val l5 = Object.of("l5")
        val l6 = Object.of("l6")
        val l7 = Object.of("l7")

        val W = Variable.of("W")
        val X = Variable.of("X")
        val Y = Variable.of("Y")
        val Z = Variable.of("Z")
    }

    object Operators {
        var moveRfromL4toL6 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))

        var moveRfromL4toL7 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL1toL2 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL1toL3 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL2toL4 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL3toL4 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL4toL5 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))

        var moveRfromL2toL6 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL5toL6 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL5toL7 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL1toL5 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))

        var moveRfromL2toL1 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL3toL1 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL4toL2 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL5toL4 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL6toL2 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL6L5 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))

        var moveRfromL7toL5 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL5toL1 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
        var moveRfromL4toL3 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))

        var loadC1fromL1onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
        var loadC1fromL2onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
        var loadC1fromL3onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
        var loadC1fromL4onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
        var loadC1fromL5onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
        var loadC1fromL6onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
        var loadC1fromL7onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))

        var loadC2fromL1onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
        var loadC2fromL2onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
        var loadC2fromL3onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
        var loadC2fromL4onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
        var loadC2fromL5onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
        var loadC2fromL6onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
        var loadC2fromL7onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))

        var unloadC1fromRtoL1 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
        var unloadC1fromRtoL2 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
        var unloadC1fromRtoL3 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
        var unloadC1fromRtoL4 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
        var unloadC1fromRtoL5 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
        var unloadC1fromRtoL6 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
        var unloadC1fromRtoL7 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))

        var unloadC2fromRtoL1 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
        var unloadC2fromRtoL2 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
        var unloadC2fromRtoL3 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
        var unloadC2fromRtoL4 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
        var unloadC2fromRtoL5 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
        var unloadC2fromRtoL6 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
        var unloadC2fromRtoL7 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
        init {
            unloadC1fromRtoL1 = unloadC1fromRtoL1.apply(VariableAssignment.of(Values.Z, Values.l1))
            unloadC1fromRtoL1 = unloadC1fromRtoL1.apply(VariableAssignment.of(Values.Y, Values.c1))

            unloadC1fromRtoL2 = unloadC1fromRtoL2.apply(VariableAssignment.of(Values.Z, Values.l2))
            unloadC1fromRtoL2 = unloadC1fromRtoL2.apply(VariableAssignment.of(Values.Y, Values.c1))

            unloadC1fromRtoL3 = unloadC1fromRtoL3.apply(VariableAssignment.of(Values.Z, Values.l3))
            unloadC1fromRtoL3 = unloadC1fromRtoL3.apply(VariableAssignment.of(Values.Y, Values.c1))

            unloadC1fromRtoL4 = unloadC1fromRtoL4.apply(VariableAssignment.of(Values.Z, Values.l4))
            unloadC1fromRtoL4 = unloadC1fromRtoL4.apply(VariableAssignment.of(Values.Y, Values.c1))

            unloadC1fromRtoL5 = unloadC1fromRtoL5.apply(VariableAssignment.of(Values.Z, Values.l5))
            unloadC1fromRtoL5 = unloadC1fromRtoL5.apply(VariableAssignment.of(Values.Y, Values.c1))

            unloadC1fromRtoL6 = unloadC1fromRtoL6.apply(VariableAssignment.of(Values.Z, Values.l6))
            unloadC1fromRtoL6 = unloadC1fromRtoL6.apply(VariableAssignment.of(Values.Y, Values.c1))

            unloadC1fromRtoL7 = unloadC1fromRtoL7.apply(VariableAssignment.of(Values.Z, Values.l7))
            unloadC1fromRtoL7 = unloadC1fromRtoL7.apply(VariableAssignment.of(Values.Y, Values.c1))

            unloadC2fromRtoL1 = unloadC2fromRtoL1.apply(VariableAssignment.of(Values.Z, Values.l1))
            unloadC2fromRtoL1 = unloadC2fromRtoL1.apply(VariableAssignment.of(Values.Y, Values.c2))

            unloadC2fromRtoL2 = unloadC2fromRtoL2.apply(VariableAssignment.of(Values.Z, Values.l2))
            unloadC2fromRtoL2 = unloadC2fromRtoL2.apply(VariableAssignment.of(Values.Y, Values.c2))

            unloadC2fromRtoL3 = unloadC2fromRtoL3.apply(VariableAssignment.of(Values.Z, Values.l3))
            unloadC2fromRtoL3 = unloadC2fromRtoL3.apply(VariableAssignment.of(Values.Y, Values.c2))

            unloadC2fromRtoL4 = unloadC2fromRtoL4.apply(VariableAssignment.of(Values.Z, Values.l4))
            unloadC2fromRtoL4 = unloadC2fromRtoL4.apply(VariableAssignment.of(Values.Y, Values.c2))

            unloadC2fromRtoL5 = unloadC2fromRtoL5.apply(VariableAssignment.of(Values.Z, Values.l5))
            unloadC2fromRtoL5 = unloadC2fromRtoL5.apply(VariableAssignment.of(Values.Y, Values.c2))

            unloadC2fromRtoL6 = unloadC2fromRtoL6.apply(VariableAssignment.of(Values.Z, Values.l6))
            unloadC2fromRtoL6 = unloadC2fromRtoL6.apply(VariableAssignment.of(Values.Y, Values.c2))

            unloadC2fromRtoL7 = unloadC2fromRtoL7.apply(VariableAssignment.of(Values.Z, Values.l7))
            unloadC2fromRtoL7 = unloadC2fromRtoL7.apply(VariableAssignment.of(Values.Y, Values.c2))

            loadC1fromL1onR = loadC1fromL1onR.apply(VariableAssignment.of(Values.Z, Values.l1))
            loadC1fromL1onR = loadC1fromL1onR.apply(VariableAssignment.of(Values.Y, Values.c1))

            loadC1fromL2onR = loadC1fromL2onR.apply(VariableAssignment.of(Values.Z, Values.l2))
            loadC1fromL2onR = loadC1fromL2onR.apply(VariableAssignment.of(Values.Y, Values.c1))

            loadC1fromL3onR = loadC1fromL3onR.apply(VariableAssignment.of(Values.Z, Values.l3))
            loadC1fromL3onR = loadC1fromL3onR.apply(VariableAssignment.of(Values.Y, Values.c1))

            loadC1fromL4onR = loadC1fromL4onR.apply(VariableAssignment.of(Values.Z, Values.l4))
            loadC1fromL4onR = loadC1fromL4onR.apply(VariableAssignment.of(Values.Y, Values.c1))

            loadC1fromL5onR = loadC1fromL5onR.apply(VariableAssignment.of(Values.Z, Values.l5))
            loadC1fromL5onR = loadC1fromL5onR.apply(VariableAssignment.of(Values.Y, Values.c1))

            loadC1fromL6onR = loadC1fromL6onR.apply(VariableAssignment.of(Values.Z, Values.l6))
            loadC1fromL6onR = loadC1fromL6onR.apply(VariableAssignment.of(Values.Y, Values.c1))

            loadC1fromL7onR = loadC1fromL7onR.apply(VariableAssignment.of(Values.Z, Values.l7))
            loadC1fromL7onR = loadC1fromL7onR.apply(VariableAssignment.of(Values.Y, Values.c1))

            loadC2fromL1onR = loadC2fromL1onR.apply(VariableAssignment.of(Values.Z, Values.l1))
            loadC2fromL1onR = loadC2fromL1onR.apply(VariableAssignment.of(Values.Y, Values.c2))

            loadC2fromL2onR = loadC2fromL2onR.apply(VariableAssignment.of(Values.Z, Values.l2))
            loadC2fromL2onR = loadC2fromL2onR.apply(VariableAssignment.of(Values.Y, Values.c2))

            loadC2fromL3onR = loadC2fromL3onR.apply(VariableAssignment.of(Values.Z, Values.l3))
            loadC2fromL3onR = loadC2fromL3onR.apply(VariableAssignment.of(Values.Y, Values.c2))

            loadC2fromL4onR = loadC2fromL4onR.apply(VariableAssignment.of(Values.Z, Values.l4))
            loadC2fromL4onR = loadC2fromL4onR.apply(VariableAssignment.of(Values.Y, Values.c2))

            loadC2fromL5onR = loadC2fromL5onR.apply(VariableAssignment.of(Values.Z, Values.l5))
            loadC2fromL5onR = loadC2fromL5onR.apply(VariableAssignment.of(Values.Y, Values.c2))

            loadC2fromL6onR = loadC2fromL6onR.apply(VariableAssignment.of(Values.Z, Values.l6))
            loadC2fromL6onR = loadC2fromL6onR.apply(VariableAssignment.of(Values.Y, Values.c2))

            loadC2fromL7onR = loadC2fromL7onR.apply(VariableAssignment.of(Values.Z, Values.l7))
            loadC2fromL7onR = loadC2fromL7onR.apply(VariableAssignment.of(Values.Y, Values.c2))

            moveRfromL1toL2 = moveRfromL1toL2.apply(VariableAssignment.of(Values.Y, Values.l1))
            moveRfromL1toL2 = moveRfromL1toL2.apply(VariableAssignment.of(Values.Z, Values.l2))

            moveRfromL2toL4 = moveRfromL2toL4.apply(VariableAssignment.of(Values.Y, Values.l2))
            moveRfromL2toL4 = moveRfromL2toL4.apply(VariableAssignment.of(Values.Z, Values.l4))

            moveRfromL3toL4 = moveRfromL3toL4.apply(VariableAssignment.of(Values.Y, Values.l3))
            moveRfromL3toL4 = moveRfromL3toL4.apply(VariableAssignment.of(Values.Z, Values.l4))

            moveRfromL4toL5 = moveRfromL4toL5.apply(VariableAssignment.of(Values.Y, Values.l4))
            moveRfromL4toL5 = moveRfromL4toL5.apply(VariableAssignment.of(Values.Z, Values.l5))

            moveRfromL2toL6 = moveRfromL2toL6.apply(VariableAssignment.of(Values.Y, Values.l2))
            moveRfromL2toL6 = moveRfromL2toL6.apply(VariableAssignment.of(Values.Z, Values.l6))

            moveRfromL5toL6 = moveRfromL5toL6.apply(VariableAssignment.of(Values.Y, Values.l5))
            moveRfromL5toL6 = moveRfromL5toL6.apply(VariableAssignment.of(Values.Z, Values.l6))

            moveRfromL5toL7 = moveRfromL5toL7.apply(VariableAssignment.of(Values.Y, Values.l5))
            moveRfromL5toL7 = moveRfromL5toL7.apply(VariableAssignment.of(Values.Z, Values.l7))

            moveRfromL2toL1 = moveRfromL2toL1.apply(VariableAssignment.of(Values.Y, Values.l2))
            moveRfromL2toL1 = moveRfromL2toL1.apply(VariableAssignment.of(Values.Z, Values.l1))

            moveRfromL3toL1 = moveRfromL3toL1.apply(VariableAssignment.of(Values.Y, Values.l3))
            moveRfromL3toL1 = moveRfromL3toL1.apply(VariableAssignment.of(Values.Z, Values.l1))

            moveRfromL4toL2 = moveRfromL4toL2.apply(VariableAssignment.of(Values.Y, Values.l4))
            moveRfromL4toL2 = moveRfromL4toL2.apply(VariableAssignment.of(Values.Z, Values.l2))

            moveRfromL4toL3 = moveRfromL4toL3.apply(VariableAssignment.of(Values.Y, Values.l4))
            moveRfromL4toL3 = moveRfromL4toL3.apply(VariableAssignment.of(Values.Z, Values.l3))

            moveRfromL5toL4 = moveRfromL5toL4.apply(VariableAssignment.of(Values.Y, Values.l5))
            moveRfromL5toL4 = moveRfromL5toL4.apply(VariableAssignment.of(Values.Z, Values.l4))

            moveRfromL6toL2 = moveRfromL6toL2.apply(VariableAssignment.of(Values.Y, Values.l6))
            moveRfromL6toL2 = moveRfromL6toL2.apply(VariableAssignment.of(Values.Z, Values.l2))

            moveRfromL6L5 = moveRfromL6L5.apply(VariableAssignment.of(Values.Y, Values.l6))
            moveRfromL6L5 = moveRfromL6L5.apply(VariableAssignment.of(Values.Z, Values.l5))

            moveRfromL7toL5 = moveRfromL7toL5.apply(VariableAssignment.of(Values.Y, Values.l7))
            moveRfromL7toL5 = moveRfromL7toL5.apply(VariableAssignment.of(Values.Z, Values.l5))

            moveRfromL1toL5 = moveRfromL1toL5.apply(VariableAssignment.of(Values.Y, Values.l1))
            moveRfromL1toL5 = moveRfromL1toL5.apply(VariableAssignment.of(Values.Z, Values.l5))

            moveRfromL5toL1 = moveRfromL5toL1.apply(VariableAssignment.of(Values.Y, Values.l5))
            moveRfromL5toL1 = moveRfromL5toL1.apply(VariableAssignment.of(Values.Z, Values.l1))

            moveRfromL1toL3 = moveRfromL1toL3.apply(VariableAssignment.of(Values.Y, Values.l1))
            moveRfromL1toL3 = moveRfromL1toL3.apply(VariableAssignment.of(Values.Z, Values.l3))

            moveRfromL3toL1 = moveRfromL3toL1.apply(VariableAssignment.of(Values.Y, Values.l3))
            moveRfromL3toL1 = moveRfromL3toL1.apply(VariableAssignment.of(Values.Z, Values.l1))

            moveRfromL4toL7 = moveRfromL4toL7.apply(VariableAssignment.of(Values.Y, Values.l4))
            moveRfromL4toL7 = moveRfromL4toL7.apply(VariableAssignment.of(Values.Z, Values.l7))

            moveRfromL4toL6 = moveRfromL4toL6.apply(VariableAssignment.of(Values.Y, Values.l4))
            moveRfromL4toL6 = moveRfromL4toL6.apply(VariableAssignment.of(Values.Z, Values.l6))
        }
    }
}
