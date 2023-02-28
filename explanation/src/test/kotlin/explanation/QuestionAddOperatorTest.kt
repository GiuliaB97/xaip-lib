package explanation

import core.* // ktlint-disable no-wildcard-imports
import domain.BlockWorldDomain.Actions
import domain.BlockWorldDomain.Operators.pickA
import domain.BlockWorldDomain.Operators.pickB
import domain.BlockWorldDomain.Operators.pickC
import domain.BlockWorldDomain.Operators.stackAB
import domain.BlockWorldDomain.Problems
import domain.LogisticsDomain
import domain.LogisticsDomain.Operators.loadC1fromL2onR
import domain.LogisticsDomain.Operators.moveRfromL1toL2
import domain.LogisticsDomain.Operators.moveRfromL2toL1
import domain.LogisticsDomain.Operators.moveRfromL2toL4
import domain.LogisticsDomain.Operators.unloadC1fromRtoL4
import explanation.impl.QuestionAddOperator
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
class QuestionAddOperatorTest : AnnotationSpec() {

    @Test
    fun `Add pickA to stackAB plan (problem stackAB)`() {
        val q1 = QuestionAddOperator(
            Problems.stackAB,
            Plan.of(listOf(stackAB)),
            pickA,
            0,
        )

        val explanation = Explainer.of(Planner.strips()).explain(q1)
        explanation.originalPlan shouldBe q1.plan
        explanation.novelPlan shouldBe Plan.of(listOf(pickA, stackAB))
        explanation.addedList shouldBe listOf(pickA)
        explanation.deletedList shouldBe emptyList()
        explanation.sharedList shouldBe listOf(stackAB)
        explanation.isPlanValid() shouldBe true
    }

    @Test
    fun `Execute pickC to reach the goal (pickC replace pickB in the plan to solve armNotEmptyProblem)`() {
        val q1 = QuestionAddOperator(
            Problems.armNotEmpty,
            Plan.of(listOf(pickB)),
            pickC,
            1,
        )

        val explanation = Explainer.of(Planner.strips()).explain(q1)
        explanation.originalPlan shouldBe q1.plan
        explanation.novelPlan shouldBe Plan.of(listOf(pickB, pickC))
        explanation.addedList shouldBe listOf(pickC)
        explanation.deletedList shouldBe emptyList()
        explanation.sharedList shouldBe listOf(pickB)
        explanation.isPlanValid() shouldBe false
    }

    @Test
    fun `Add useless operator (pickA) to the plan pickC in pickC problem`() {
        val q1 = QuestionAddOperator(
            Problems.pickC,
            Plan.of(listOf(pickC)),
            pickA,
            0,
        )

        val explanation = Explainer.of(Planner.strips()).explain(q1)
        explanation.originalPlan shouldBe q1.plan

        explanation.novelPlan shouldBe Plan.of(listOf(pickA, pickC))
        explanation.addedList shouldBe listOf(pickA)
        explanation.deletedList shouldBe emptyList()
        explanation.sharedList shouldBe listOf(pickC)
        explanation.isPlanValid() shouldBe false
    }

    @Test
    fun `Add useless operator (moveRfromL2toL1) to the plan`() {
        val q1 = QuestionAddOperator(
            LogisticsDomain.Problems.robotFromLoc1ToLoc2ContainerFromLocation2ToLocation4,
            Plan.of(
                listOf(
                    moveRfromL1toL2,
                    loadC1fromL2onR,
                    moveRfromL2toL4,
                    unloadC1fromRtoL4,
                ),
            ),
            moveRfromL2toL1,
            0,
        )

        val explanation = Explainer.of(Planner.strips()).explain(q1)
        explanation.isPlanLengthAcceptable() shouldBe true
        explanation.isProblemSolvable() shouldBe true
        explanation.isPlanValid() shouldBe false
    }

    @Test
    fun `Basic Add useless operator (moveRfromL2toL1) to the plan`() {
        val q1 = QuestionAddOperator(
            LogisticsDomain.Problems.basicRobotFromLocation1ToLocation2,
            Plan.of(
                listOf(
                    moveRfromL1toL2,
                    moveRfromL2toL1,
                ),
            ),
            moveRfromL2toL1,
            1,
        )

        val explanation = Explainer.of(Planner.strips()).explain(q1)
        explanation.isPlanLengthAcceptable() shouldBe true
        explanation.isProblemSolvable() shouldBe true
        explanation.isPlanValid() shouldBe false
    }

    @Test
    fun `testBuildDomain`() {
        val predicate = Predicate.of("has_done_" + pickA.name, pickA.parameters.values.toList())
        val fluent = Fluent.positive(predicate, *pickA.parameters.keys.toTypedArray())

        QuestionAddOperator(
            Problems.stackAB,
            Plan.of(listOf(stackAB)),
            pickA,
            0,
        ).buildHypotheticalDomain() shouldBe
            Domain.of(
                Problems.stackAB.domain.name,
                mutableSetOf(predicate).also { it.addAll(Problems.stackAB.domain.predicates) },
                mutableSetOf(
                    Action.of(
                        name = Actions.pick.name + "^",
                        parameters = Actions.pick.parameters,
                        preconditions = Actions.pick.preconditions,
                        effects = mutableSetOf(Effect.of(fluent)).also { it.addAll(Actions.pick.effects) },
                    ),
                ).also { it.addAll(Problems.stackAB.domain.actions) }.also { it.remove(Actions.pick) },
                Problems.stackAB.domain.types,
            )
    }

    @Test
    fun `testBuildProblem`() {
        val predicate = Predicate.of("has_done_" + pickA.name, pickA.parameters.values.toList())
        val fluent = Fluent.positive(predicate, *pickA.parameters.keys.toTypedArray())
        val groundFluent = Fluent.positive(predicate, *pickA.args.toTypedArray())

        QuestionAddOperator(
            Problems.stackAB,
            Plan.of(listOf(stackAB)),
            pickA,
            0,
        ).buildHypotheticalProblem().first() shouldBe Problem.of(
            Domain.of(
                Problems.stackAB.domain.name,
                mutableSetOf(predicate).also { it.addAll(Problems.stackAB.domain.predicates) },
                mutableSetOf(
                    Action.of(
                        name = Actions.pick.name + "^",
                        parameters = Actions.pick.parameters,
                        preconditions = Actions.pick.preconditions,
                        effects = mutableSetOf(Effect.of(fluent)).also { it.addAll(Actions.pick.effects) },
                    ),
                ).also { it.addAll(Problems.stackAB.domain.actions) }.also { it.remove(Actions.pick) },
                Problems.stackAB.domain.types,
            ),
            Problems.stackAB.objects,
            Problems.stackAB.initialState,
            FluentBasedGoal.of(
                (Problems.stackAB.goal as FluentBasedGoal).targets.toMutableSet()
                    .also { it.add(groundFluent) },
            ),
        )
    }
}
