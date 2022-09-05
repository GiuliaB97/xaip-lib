package resources.domain

import Object
object GridDomain {
    object Actions {
        val move = Action.of(
            name = "move",
            parameters = mapOf(
                Values.X to Types.robots,
                Values.Y to Types.locations,
                Values.Z to Types.locations
            ),
            preconditions = setOf(Fluents.adjacentYZ, Fluents.atRobotXlocationY),
            effects = setOf(
                Effect.of(Fluents.atRobotXlocationW),
                Effect.negative(Fluents.atRobotXlocationY)
            )
        )
        val load = Action.of(
            name = "load",
            parameters = mapOf(
                Values.Z to Types.locations,
                Values.Y to Types.containers,
                Values.X to Types.robots
            ),
            preconditions = setOf(Fluents.atRobotXlocationZ, Fluents.inContainerYlocationZ, Fluents.unloadedX),
            effects = setOf(
                Effect.of(Fluents.loadedXY),
                Effect.negative(Fluents.inContainerYlocationZ),
                Effect.negative(Fluents.unloadedX)
            )
        )
        val unload = Action.of(
            name = "unload",
            parameters = mapOf(
                Values.Z to Types.locations,
                Values.Y to Types.containers,
                Values.X to Types.robots
            ),
            preconditions = setOf(Fluents.atRobotXlocationZ, Fluents.loadedXY),
            effects = setOf(
                Effect.of(Fluents.inContainerYlocationZ),
                Effect.of(Fluents.unloadedX),
                Effect.negative(Fluents.loadedXY)
            )
        )
    }

    object Domains {
        val gridWorld = Domain.of(
            name = "grid_world",
            predicates = setOf(
                Predicates.adjacent,
                Predicates.atLocation,
                Predicates.loaded,
                Predicates.unLoaded,
                Predicates.inContainerLocation
            ),
            actions = setOf(
                Actions.move,
                Actions.load,
                Actions.unload
            ),
            types = setOf(
                Types.anything,
                Types.strings,
                Types.locations,
                Types.robots,
                Types.containers
            )
        )
    }

    object Fluents {
        val atRobotlocation1 = Fluent.positive(Predicates.atLocation, Values.r, Values.l1)
        val atRobotlocation2 = Fluent.positive(Predicates.atLocation, Values.r, Values.l2)
        val atRobotlocation3 = Fluent.positive(Predicates.atLocation, Values.r, Values.l3)
        val atRobotlocation4 = Fluent.positive(Predicates.atLocation, Values.r, Values.l4)
        val atRobotXlocationY = Fluent.positive(Predicates.atLocation, Values.X, Values.Y)
        val atRobotXlocationZ = Fluent.positive(Predicates.atLocation, Values.X, Values.Z)
        val atRobotXlocationW = Fluent.positive(Predicates.atLocation, Values.X, Values.W)

        val adjacentL1L2 = Fluent.positive(Predicates.adjacent, Values.l1, Values.l2)
        val adjacentL1L3 = Fluent.positive(Predicates.adjacent, Values.l1, Values.l3)
        val adjacentL2L4 = Fluent.positive(Predicates.adjacent, Values.l2, Values.l4)
        val adjacentL3L4 = Fluent.positive(Predicates.adjacent, Values.l1, Values.l2)
        val adjacentXY = Fluent.positive(Predicates.adjacent, Values.X, Values.Y)
        val adjacentXZ = Fluent.positive(Predicates.adjacent, Values.X, Values.Z)
        val adjacentXW = Fluent.positive(Predicates.adjacent, Values.X, Values.W)
        val adjacentYZ = Fluent.positive(Predicates.adjacent, Values.Y, Values.Z)
        val adjacentYW = Fluent.positive(Predicates.adjacent, Values.Y, Values.W)
        val adjacentZW = Fluent.positive(Predicates.adjacent, Values.Z, Values.W)

        val loadedXY = Fluent.positive(Predicates.loaded, Values.X, Values.Y)

        val unloadedX = Fluent.positive(Predicates.unLoaded, Values.X)
        val unloadedY = Fluent.positive(Predicates.unLoaded, Values.Y)
        val unloadedZ = Fluent.positive(Predicates.unLoaded, Values.Z)
        val unloadedW = Fluent.positive(Predicates.unLoaded, Values.W)

        val inContainerlocation1 = Fluent.positive(Predicates.inContainerLocation, Values.c, Values.l1)
        val inContainerlocation2 = Fluent.positive(Predicates.inContainerLocation, Values.c, Values.l2)
        val inContainerlocation3 = Fluent.positive(Predicates.inContainerLocation, Values.c, Values.l3)
        val inContainerlocation4 = Fluent.positive(Predicates.inContainerLocation, Values.c, Values.l4)
        val inContainerXlocationY = Fluent.positive(Predicates.inContainerLocation, Values.X, Values.Y)
        val inContainerXlocationZ = Fluent.positive(Predicates.inContainerLocation, Values.X, Values.Z)
        val inContainerXlocationW = Fluent.positive(Predicates.inContainerLocation, Values.X, Values.W)
        val inContainerYlocationX = Fluent.positive(Predicates.inContainerLocation, Values.Y, Values.X)
        val inContainerYlocationZ = Fluent.positive(Predicates.inContainerLocation, Values.Y, Values.Z)
        val inContainerYlocationW = Fluent.positive(Predicates.inContainerLocation, Values.Y, Values.W)
    }

    object Goals {
        val atRobotAtlocation3 = FluentBasedGoal.of(Fluents.atRobotlocation3)
        val atRobotAtlocation3InContainerLocation4 = FluentBasedGoal.of(
            Fluents.atRobotlocation3,
            Fluents.inContainerlocation4
        )
        val inContainerLocation4 = FluentBasedGoal.of(
            Fluents.inContainerlocation4
        )
    }

    object ObjectSets {
        val all = ObjectSet.of(
            Types.robots to setOf(Values.r),
            Types.locations to setOf(Values.l1, Values.l2, Values.l3, Values.l4),
            Types.containers to setOf(Values.c)
        )
    }

    object Problems {
        val robotFromLoc1ToLoc2 = Problem.of(
            domain = Domains.gridWorld,
            objects = ObjectSets.all,
            initialState = States.initial,
            goal = Goals.atRobotAtlocation3
        )
        val robotFromLoc1ToLoc2ContainerFromLocation2ToLocation4 = Problem.of(
            domain = Domains.gridWorld,
            objects = ObjectSets.all,
            initialState = States.initial,
            goal = Goals.atRobotAtlocation3InContainerLocation4
        )
        val inContainerLocation4 = Problem.of(
            domain = Domains.gridWorld,
            objects = ObjectSets.all,
            initialState = States.initial,
            goal = Goals.inContainerLocation4
        )
    }

    object Predicates {
        val adjacent = Predicate.of("adjacent", Types.locations, Types.locations)
        val atLocation = Predicate.of("atLocation", Types.robots, Types.locations)
        val loaded = Predicate.of("loaded", Types.robots, Types.containers)
        val unLoaded = Predicate.of("unloaded", Types.robots)
        val inContainerLocation = Predicate.of("inContainerLocation", Types.containers, Types.robots)
    }

    object States {
        val initial = State.of(
            Fluents.atRobotlocation1,
            Fluents.inContainerlocation2,
            Fluents.adjacentL1L2,
            Fluents.adjacentL1L3,
            Fluents.adjacentL2L4,
            Fluents.adjacentL3L4
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

        val c = Object.of("c")

        val l1 = Object.of("l1")
        val l2 = Object.of("l2")
        val l3 = Object.of("l3")
        val l4 = Object.of("l4")

        val W = Variable.of("W")
        val X = Variable.of("X")
        val Y = Variable.of("Y")
        val Z = Variable.of("Z")
    }
}