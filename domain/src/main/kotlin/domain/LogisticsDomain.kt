package domain

import core.* // ktlint-disable no-wildcard-imports
import dsl.domain
import dsl.problem

object LogisticsDomain {
    val fluents by lazy {
        setOf(
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
    }

    val problems by lazy {
        arrayOf(
            Problems.basicRobotFromLocation1ToLocation2,
            Problems.inContainerLocation4,
            Problems.robotFromLoc1ToLoc2,
            Problems.robotFromLoc1ToLoc5Container1FromLoc2ToLoc4Container2FromLoc3ToLoc1,
            Problems.robotFromLoc1ToLoc5C1fromLoc2toLoc4,
            Problems.robotFromLoc1ToLoc2ContainerFromLocation2ToLocation4,
        )
    }

    val variables by lazy {
        arrayOf(Values.X, Values.Y, Values.Z, Values.W)
    }

    val problemNames by lazy { BlockWorldDomain.problems.map { it.name } }

    object DomainsDSL {
        val logistics by lazy {
            domain {
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
    }

    object ProblemsDSL {
        val rToX by lazy {
            problem(DomainsDSL.logistics) {
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
        }
        val robotFromLoc1ToLoc5Container1FromLoc2ToLoc4Container2FromLoc3ToLoc1 by lazy {
            problem(DomainsDSL.logistics) {
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
        }
        val robotFromLoc1ToLoc5Container1FromLoc2ToLoc4Container2FromLoc3ToLoc1notDSL by lazy {
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
    }

    object Actions {
        val move by lazy {
            Action.of(
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
        }
        val load by lazy {
            Action.of(
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
        }
        val unload by lazy {
            Action.of(
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
    }

    object Domains {
        val logisticsWorld by lazy {
            Domain.of(
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
    }

    object Fluents {
        val atRlocationY by lazy { Fluent.positive(Predicates.atLocation, Values.r, Values.Y) }
        val atRobotlocation1 by lazy { Fluent.positive(Predicates.atLocation, Values.r, Values.l1) }
        val atRobotlocation2 by lazy { Fluent.positive(Predicates.atLocation, Values.r, Values.l2) }
        val atRobotlocation3 by lazy { Fluent.positive(Predicates.atLocation, Values.r, Values.l3) }
        val atRobotlocation4 by lazy { Fluent.positive(Predicates.atLocation, Values.r, Values.l4) }
        val atRobotlocation5 by lazy { Fluent.positive(Predicates.atLocation, Values.r, Values.l5) }
        val atRobotlocation6 by lazy { Fluent.positive(Predicates.atLocation, Values.r, Values.l6) }
        val atRobotlocation7 by lazy { Fluent.positive(Predicates.atLocation, Values.r, Values.l7) }

        val atRobotXlocationY by lazy { Fluent.positive(Predicates.atLocation, Values.X, Values.Y) }
        val atRobotXlocationZ by lazy { Fluent.positive(Predicates.atLocation, Values.X, Values.Z) }
        val atRobotXlocationW by lazy { Fluent.positive(Predicates.atLocation, Values.X, Values.W) }

        val connectedL1L2 by lazy { Fluent.positive(Predicates.connected, Values.l1, Values.l2) }
        val connectedL1L3 by lazy { Fluent.positive(Predicates.connected, Values.l1, Values.l3) }
        val connectedL1L5 by lazy { Fluent.positive(Predicates.connected, Values.l1, Values.l5) }

        val connectedL2L1 by lazy { Fluent.positive(Predicates.connected, Values.l2, Values.l1) }
        val connectedL2L4 by lazy { Fluent.positive(Predicates.connected, Values.l2, Values.l4) }
        val connectedL2L6 by lazy { Fluent.positive(Predicates.connected, Values.l2, Values.l6) }

        val connectedL3L1 by lazy { Fluent.positive(Predicates.connected, Values.l3, Values.l1) }
        val connectedL3L4 by lazy { Fluent.positive(Predicates.connected, Values.l3, Values.l4) }

        val connectedL4L2 by lazy { Fluent.positive(Predicates.connected, Values.l4, Values.l2) }
        val connectedL4L3 by lazy { Fluent.positive(Predicates.connected, Values.l4, Values.l3) }
        val connectedL4L5 by lazy { Fluent.positive(Predicates.connected, Values.l4, Values.l5) }

        val connectedL5L1 by lazy { Fluent.positive(Predicates.connected, Values.l5, Values.l1) }
        val connectedL5L4 by lazy { Fluent.positive(Predicates.connected, Values.l5, Values.l4) }
        val connectedL5L6 by lazy { Fluent.positive(Predicates.connected, Values.l5, Values.l6) }
        val connectedL5L7 by lazy { Fluent.positive(Predicates.connected, Values.l5, Values.l7) }

        val connectedL6L2 by lazy { Fluent.positive(Predicates.connected, Values.l6, Values.l2) }
        val connectedL6L5 by lazy { Fluent.positive(Predicates.connected, Values.l6, Values.l5) }
        val connectedL7L5 by lazy { Fluent.positive(Predicates.connected, Values.l7, Values.l5) }

        val connectedL4L6 by lazy { Fluent.positive(Predicates.connected, Values.l4, Values.l6) }
        val connectedL6L4 by lazy { Fluent.positive(Predicates.connected, Values.l6, Values.l4) }

        val connectedXY by lazy { Fluent.positive(Predicates.connected, Values.X, Values.Y) }
        val connectedXZ by lazy { Fluent.positive(Predicates.connected, Values.X, Values.Z) }
        val connectedXW by lazy { Fluent.positive(Predicates.connected, Values.X, Values.W) }
        val connectedYW by lazy { Fluent.positive(Predicates.connected, Values.Y, Values.W) }
        val connectedYZ by lazy { Fluent.positive(Predicates.connected, Values.Y, Values.Z) }
        val connectedZW by lazy { Fluent.positive(Predicates.connected, Values.Z, Values.W) }

        val loadedXY by lazy { Fluent.positive(Predicates.loaded, Values.X, Values.Y) }

        val unloadedX by lazy { Fluent.positive(Predicates.unloaded, Values.X) }
        val unloadedY by lazy { Fluent.positive(Predicates.unloaded, Values.Y) }
        val unloadedZ by lazy { Fluent.positive(Predicates.unloaded, Values.Z) }
        val unloadedW by lazy { Fluent.positive(Predicates.unloaded, Values.W) }

        val inContainer1location1 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l1) }
        val inContainer1location2 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l2) }
        val inContainer1location3 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l3) }
        val inContainer1location4 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l4) }
        val inContainer1location5 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l5) }
        val inContainer1location6 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l6) }
        val inContainer1location7 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c1, Values.l7) }

        val inContainer2location1 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l1) }
        val inContainer2location2 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l2) }
        val inContainer2location3 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l3) }
        val inContainer2location4 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l4) }
        val inContainer2location5 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l5) }
        val inContainer2location6 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l6) }
        val inContainer2location7 by lazy { Fluent.positive(Predicates.inContainerLocation, Values.c2, Values.l7) }

        val inContainerXlocationY by lazy { Fluent.positive(Predicates.inContainerLocation, Values.X, Values.Y) }
        val inContainerXlocationZ by lazy { Fluent.positive(Predicates.inContainerLocation, Values.X, Values.Z) }
        val inContainerXlocationW by lazy { Fluent.positive(Predicates.inContainerLocation, Values.X, Values.W) }
        val inContainerYlocationX by lazy { Fluent.positive(Predicates.inContainerLocation, Values.Y, Values.X) }
        val inContainerYlocationZ by lazy { Fluent.positive(Predicates.inContainerLocation, Values.Y, Values.Z) }
        val inContainerYlocationW by lazy { Fluent.positive(Predicates.inContainerLocation, Values.Y, Values.W) }
    }

    object Goals {
        val atRobotAtlocation5atC1loc4 by lazy {
            FluentBasedGoal.of(
                Fluents.atRobotlocation5,
                Fluents.inContainer1location4,
            )
        }
        val atRobotAtlocation2 by lazy { FluentBasedGoal.of(Fluents.atRobotlocation2) }
        val atRobotAtLocationY by lazy { FluentBasedGoal.of(Fluents.atRlocationY) }
        val atRobotAtlocation3 by lazy { FluentBasedGoal.of(Fluents.atRobotlocation3) }
        val inContainerLocation4 by lazy {
            FluentBasedGoal.of(
                Fluents.inContainer1location4,
            )
        }
        val atRobotAtlocation3InContainer1Location4 by lazy {
            FluentBasedGoal.of(
                Fluents.atRobotlocation4,
                Fluents.inContainer1location4,
            )
        }

        val atRobotAtlocation5inContainer1Location4InContainer2Location1 by lazy {
            FluentBasedGoal.of(
                Fluents.atRobotlocation5,
                Fluents.inContainer1location4,
                Fluents.inContainer2location1,
            )
        }
    }

    object ObjectSets {
        val all by lazy {
            ObjectSet.of(
                Types.robots to setOf(Values.r),
                Types.locations to setOf(Values.l1, Values.l2, Values.l3, Values.l4, Values.l5, Values.l6, Values.l7),
                Types.containers to setOf(Values.c1, Values.c2),
            )
        }
    }

    object Problems {

        val rToXdslDomain by lazy {
            Problem.of(
                domain = DomainsDSL.logistics,
                name = "rToXdslDomain",
                objects = ObjectSets.all,
                initialState = States.initial,
                goal = Goals.atRobotAtlocation5inContainer1Location4InContainer2Location1,
            )
        }

        val rToX by lazy {
            Problem.of(
                domain = Domains.logisticsWorld,
                name = "rToX",
                objects = ObjectSets.all,
                initialState = States.initial,
                goal = Goals.atRobotAtLocationY,
            )
        }

        val robotFromLoc1ToLoc2 by lazy {
            Problem.of(
                domain = Domains.logisticsWorld,
                name = "robotFromLoc1ToLoc2",
                objects = ObjectSets.all,
                initialState = States.initial,
                goal = Goals.atRobotAtlocation2,
            )
        }

        val robotFromLoc1ToLoc5C1fromLoc2toLoc4 by lazy {
            Problem.of(
                domain = Domains.logisticsWorld,
                name = "robotFromLoc1ToLoc5C1fromLoc2toLoc4",
                objects = ObjectSets.all,
                initialState = States.initial,
                goal = Goals.atRobotAtlocation5atC1loc4,
            )
        }

        val inContainerLocation4 by lazy {
            Problem.of(
                domain = Domains.logisticsWorld,
                name = "inContainerLocation4",
                objects = ObjectSets.all,
                initialState = States.initial,
                goal = Goals.inContainerLocation4,
            )
        }

        val robotFromLoc1ToLoc2ContainerFromLocation2ToLocation4 by lazy {
            Problem.of(
                domain = Domains.logisticsWorld,
                name = "robotFromLoc1ToLoc2ContainerFromLocation2ToLocation4",
                objects = ObjectSets.all,
                initialState = States.initial,
                goal = Goals.atRobotAtlocation3InContainer1Location4,
            )
        }

        val robotFromLoc1ToLoc5Container1FromLoc2ToLoc4Container2FromLoc3ToLoc1 by lazy {
            Problem.of(
                domain = Domains.logisticsWorld,
                name = "robotFromLoc1ToLoc5Container1FromLoc2ToLoc4Container2FromLoc3ToLoc1",
                objects = ObjectSets.all,
                initialState = States.initial,
                goal = Goals.atRobotAtlocation5inContainer1Location4InContainer2Location1,
            )
        }

        val basicRobotFromLocation1ToLocation2 by lazy {
            Problem.of(
                domain = Domains.logisticsWorld,
                name = "basicRobotFromLocation1ToLocation2",
                objects = ObjectSets.all,
                initialState = States.robotInLoc1,
                goal = Goals.atRobotAtlocation2,
            )
        }
    }

    object Predicates {
        val connected by lazy { Predicate.of("connected", Types.locations, Types.locations) }
        val atLocation by lazy { Predicate.of("atLocation", Types.robots, Types.locations) }
        val loaded by lazy { Predicate.of("loaded", Types.robots, Types.containers) }
        val unloaded by lazy { Predicate.of("unloaded", Types.robots) }
        val inContainerLocation by lazy { Predicate.of("inContainerLocation", Types.containers, Types.robots) }
    }

    object States {
        val robotInLoc1 by lazy {
            State.of(
                mutableSetOf(Fluents.atRobotlocation1).also { it.addAll(fluents) },
            )
        }

        val initial by lazy {
            State.of(
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
        }

        val alternativeInitialState by lazy {
            State.of(
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
        }

        val alternativeState by lazy {
            State.of(
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
    }

    object Types {
        val anything by lazy { Type.of("anything") }
        val strings by lazy { Type.of("strings", anything) }
        val locations by lazy { Type.of("locations", strings) }
        val robots by lazy { Type.of("robots", strings) }
        val containers by lazy { Type.of("containers", strings) }
    }

    object Values {
        val r by lazy { Object.of("r") }

        val c1 by lazy { Object.of("c1") }
        val c2 by lazy { Object.of("c2") }

        val l1 by lazy { Object.of("l1") }
        val l2 by lazy { Object.of("l2") }
        val l3 by lazy { Object.of("l3") }
        val l4 by lazy { Object.of("l4") }
        val l5 by lazy { Object.of("l5") }
        val l6 by lazy { Object.of("l6") }
        val l7 by lazy { Object.of("l7") }

        val W by lazy { Variable.of("W") }
        val X by lazy { Variable.of("X") }
        val Y by lazy { Variable.of("Y") }
        val Z by lazy { Variable.of("Z") }
    }

    object Operators {
        var moveRfromL4toL6: Operator
        var moveRfromL4toL7: Operator
        var moveRfromL1toL2: Operator
        var moveRfromL1toL3: Operator
        var moveRfromL2toL4: Operator
        var moveRfromL3toL4: Operator
        var moveRfromL4toL5: Operator

        var moveRfromL2toL6: Operator
        var moveRfromL5toL6: Operator
        var moveRfromL5toL7: Operator
        var moveRfromL1toL5: Operator

        var moveRfromL2toL1: Operator
        var moveRfromL3toL1: Operator
        var moveRfromL4toL2: Operator
        var moveRfromL5toL4: Operator
        var moveRfromL6toL2: Operator
        var moveRfromL6L5: Operator

        var moveRfromL7toL5: Operator
        var moveRfromL5toL1: Operator
        var moveRfromL4toL3: Operator

        var loadC1fromL1onR: Operator
        var loadC1fromL2onR: Operator
        var loadC1fromL3onR: Operator
        var loadC1fromL4onR: Operator
        var loadC1fromL5onR: Operator
        var loadC1fromL6onR: Operator
        var loadC1fromL7onR: Operator

        var loadC2fromL1onR: Operator
        var loadC2fromL2onR: Operator
        var loadC2fromL3onR: Operator
        var loadC2fromL4onR: Operator
        var loadC2fromL5onR: Operator
        var loadC2fromL6onR: Operator
        var loadC2fromL7onR: Operator

        var unloadC1fromRtoL1: Operator
        var unloadC1fromRtoL2: Operator
        var unloadC1fromRtoL3: Operator
        var unloadC1fromRtoL4: Operator
        var unloadC1fromRtoL5: Operator
        var unloadC1fromRtoL6: Operator
        var unloadC1fromRtoL7: Operator

        var unloadC2fromRtoL1: Operator
        var unloadC2fromRtoL2: Operator
        var unloadC2fromRtoL3: Operator
        var unloadC2fromRtoL4: Operator
        var unloadC2fromRtoL5: Operator
        var unloadC2fromRtoL6: Operator
        var unloadC2fromRtoL7: Operator

        init {
            moveRfromL4toL6 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL4toL7 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL1toL2 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL1toL3 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL2toL4 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL3toL4 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL4toL5 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))

            moveRfromL2toL6 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL5toL6 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL5toL7 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL1toL5 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))

            moveRfromL2toL1 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL3toL1 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL4toL2 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL5toL4 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL6toL2 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL6L5 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))

            moveRfromL7toL5 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL5toL1 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))
            moveRfromL4toL3 = Operator.of(Actions.move).apply(VariableAssignment.of(Values.X, Values.r))

            loadC1fromL1onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
            loadC1fromL2onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
            loadC1fromL3onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
            loadC1fromL4onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
            loadC1fromL5onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
            loadC1fromL6onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
            loadC1fromL7onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))

            loadC2fromL1onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
            loadC2fromL2onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
            loadC2fromL3onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
            loadC2fromL4onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
            loadC2fromL5onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
            loadC2fromL6onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))
            loadC2fromL7onR = Operator.of(Actions.load).apply(VariableAssignment.of(Values.X, Values.r))

            unloadC1fromRtoL1 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
            unloadC1fromRtoL2 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
            unloadC1fromRtoL3 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
            unloadC1fromRtoL4 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
            unloadC1fromRtoL5 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
            unloadC1fromRtoL6 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
            unloadC1fromRtoL7 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))

            unloadC2fromRtoL1 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
            unloadC2fromRtoL2 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
            unloadC2fromRtoL3 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
            unloadC2fromRtoL4 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
            unloadC2fromRtoL5 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
            unloadC2fromRtoL6 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))
            unloadC2fromRtoL7 = Operator.of(Actions.unload).apply(VariableAssignment.of(Values.X, Values.r))

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
