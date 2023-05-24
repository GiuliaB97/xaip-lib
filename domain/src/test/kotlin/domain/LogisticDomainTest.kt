package domain

import core.Plan
import domain.BlockWorldDomain.Planners
import domain.LogisticsDomain.Actions
import domain.LogisticsDomain.Operators
import domain.LogisticsDomain.Problems
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class LogisticDomainTest : AnnotationSpec() {
    private val planner = Planners.stripsPlanner

    @Test
    fun testProblemList() {
        val problems = arrayOf(
            Problems.basicRobotFromLocation1ToLocation2,
            Problems.inContainerLocation4,
            Problems.robotFromLoc1ToLoc2,
            Problems.robotFromLoc1ToLoc5Container1FromLoc2ToLoc4Container2FromLoc3ToLoc1,
            Problems.robotFromLoc1ToLoc5C1fromLoc2toLoc4,
            Problems.robotFromLoc1ToLoc2ContainerFromLocation2ToLocation4,
        )
        LogisticsDomain.problems[0] shouldBe problems[0]
    }

    @Test
    fun simpleRobotFromLoc1ToLoc2() {
        val plans1 = planner.plan(Problems.basicRobotFromLocation1ToLocation2)
        plans1.toSet().size shouldBe 1
        println(plans1.toSet())
        plans1.toSet().first().operators.first().name shouldBe Actions.move.name
    }

    @Test
    fun robotFromLoc1ToLoc2() {
        val plans1 = planner.plan(Problems.robotFromLoc1ToLoc2)
        plans1.toSet().size shouldBe 1
        println(plans1.toSet())
        plans1.toSet().first().operators.first().name shouldBe Actions.move.name
    }

    @Test
    fun inContainerLocation4() {
        val plans = planner.plan(
            Problems.inContainerLocation4,
        )
        plans.toSet().size shouldBe 1
        plans.toSet().first().operators.map { it.name }.toList() shouldBe listOf(
            Actions.move.name,
            Actions.load.name,
            Actions.move.name,
            Actions.unload.name,
        )
    }

    @Test
    fun robotFromLoc1ToLoc2ContainerFromLocation2ToLocation4() {
        val plans = planner.plan(
            Problems.robotFromLoc1ToLoc2ContainerFromLocation2ToLocation4,
        )
        plans.toSet().size shouldBe 1
        plans.toSet().first().operators.map { it.name }.toList() shouldBe listOf(
            Actions.move.name,
            Actions.load.name,
            Actions.move.name,
            Actions.unload.name,
        )
    }

    @Test
    fun atRobotAtLocation3InContainer1Location4InContainer2Location7() {
        val plans = planner.plan(
            Problems.robotFromLoc1ToLoc5Container1FromLoc2ToLoc4Container2FromLoc3ToLoc1,
        )
        plans.toSet().size shouldBe 1

        plans.toSet().first().operators.map { it.name }.toList() shouldBe listOf(
            Actions.move.name,
            Actions.load.name,
            Actions.move.name,
            Actions.unload.name,
            Actions.move.name,
            Actions.load.name,
            Actions.move.name,
            Actions.unload.name,
            Actions.move.name,
        )
        println(plans.first().operators)
    }

    @Test
    fun testRobotFromLoc1toLoc7c1fromloc2toLoc5() {
        val plans = planner.plan(Problems.robotFromLoc1ToLoc5C1fromLoc2toLoc4).toSet()
        val plan2check = setOf(
            Plan.of(
                listOf(
                    Operators.moveRfromL1toL2,
                    Operators.loadC1fromL2onR,
                    Operators.moveRfromL2toL4,
                    Operators.unloadC1fromRtoL4,
                    Operators.moveRfromL4toL5,
                ),
            ),
        )
        plans.size shouldBe 1
        plans shouldBe plan2check
    }

    @Test
    fun testDomainDSL() {
        val problem = Problems.rToXdslDomain
        val plans = planner.plan(problem).toSet()
        plans.size shouldBe 1
    }

    @Test
    fun testProblemDSL() {
        val problem = Problems.rToXdslDomain
        val plans = planner.plan(problem).toSet()
        plans.size shouldBe 1
        plans.first() shouldBe planner
            .plan(Problems.rToXdslDomain).first()
    }
}
