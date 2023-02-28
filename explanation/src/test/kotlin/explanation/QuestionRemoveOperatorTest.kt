package explanation

import core.* // ktlint-disable no-wildcard-imports
import domain.BlockWorldDomain
import domain.BlockWorldDomain.Operators.pickA
import domain.BlockWorldDomain.Operators.pickB
import domain.BlockWorldDomain.Operators.pickC
import domain.BlockWorldDomain.Operators.pickD
import domain.BlockWorldDomain.Operators.stackBA
import domain.BlockWorldDomain.Operators.stackCA
import domain.BlockWorldDomain.Operators.stackDB
import domain.BlockWorldDomain.Operators.stackDC
import domain.BlockWorldDomain.Problems
import domain.LogisticsDomain.Operators.moveRfromL1toL2
import explanation.impl.QuestionRemoveOperator
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import domain.LogisticsDomain.Problems as GraphProblem

class QuestionRemoveOperatorTest : AnnotationSpec() {

    @Test
    fun `Remove pickA from the plan to solve the armNotEmpty problem`() {
        val q2 = QuestionRemoveOperator(
            Problems.armNotEmpty,
            Plan.of(listOf(pickA)),
            pickA,
        )

        val explanation = Explainer.of(Planner.strips()).explain(q2)
        explanation.originalPlan shouldBe q2.plan
        explanation.novelPlan shouldBe Plan.of(listOf(pickB))
        explanation.addedList shouldBe listOf(pickB)
        explanation.deletedList shouldBe listOf(pickA)
        explanation.sharedList shouldBe emptyList()
        explanation.isPlanValid() shouldBe true
    }

    @Test
    fun `Remove pickB from the plan to solve the armNotEmpty problem`() {
        val q2 = QuestionRemoveOperator(
            Problems.stackDXA,
            Plan.of(listOf(pickB, stackBA, pickD, stackDB)),
            pickB,
        )

        val explanation = Explainer.of(Planner.strips()).explain(q2)
        explanation.originalPlan shouldBe q2.plan
        explanation.novelPlan shouldBe Plan.of(listOf(pickC, stackCA, pickD, stackDC))
        explanation.addedList shouldBe listOf(pickC, stackCA, stackDC)
        explanation.deletedList shouldBe listOf(pickB, stackBA, stackDB)
        explanation.sharedList shouldBe listOf(pickD)
        explanation.isPlanValid() shouldBe true
    }

    @Test
    fun `Remove moveRfromL1toL2 from the plan to solve the problem RtoX`() {
        val planRfromL1toL2 = Plan.of(listOf(moveRfromL1toL2))
        val graphProblemRtoX = GraphProblem.rToX
        val q2 = QuestionRemoveOperator(
            graphProblemRtoX,
            planRfromL1toL2,
            moveRfromL1toL2,
        )
        val explanation = Explainer.of(Planner.strips()).explain(q2)
        explanation.originalPlan shouldBe q2.plan
        explanation.novelPlan shouldBe Plan.of(emptyList())
        explanation.addedList shouldBe emptyList()
        explanation.deletedList shouldBe listOf(moveRfromL1toL2)
        explanation.sharedList shouldBe emptyList()
        explanation.isPlanValid() shouldBe true
    }

    @Test
    fun `testBuildDomain`() {
        val predicate = Predicate.of("not_done_" + pickA.name, pickA.parameters.values.toList())
        val fluent = Fluent.positive(predicate, *pickA.parameters.keys.toTypedArray())

        QuestionRemoveOperator(
            Problems.stackAB,
            Plan.of(listOf(BlockWorldDomain.Operators.stackAB)),
            pickA,
        ).buildHypotheticalDomain() shouldBe
            Domain.of(
                Problems.stackAB.domain.name,
                mutableSetOf(predicate).also { it.addAll(Problems.stackAB.domain.predicates) },
                mutableSetOf(
                    Action.of(
                        name = BlockWorldDomain.Actions.pick.name + "^",
                        parameters = BlockWorldDomain.Actions.pick.parameters,
                        preconditions = BlockWorldDomain.Actions.pick.preconditions,
                        effects = mutableSetOf(Effect.negative(fluent))
                            .also { it.addAll(BlockWorldDomain.Actions.pick.effects) },
                    ),
                ).also { it.addAll(Problems.stackAB.domain.actions) }.also { it.remove(BlockWorldDomain.Actions.pick) },
                Problems.stackAB.domain.types,
            )
    }

    @Test
    fun `testBuildProblem`() {
        val predicate = Predicate.of("not_done_" + pickA.name, pickA.parameters.values.toList())
        val fluent = Fluent.positive(predicate, *pickA.parameters.keys.toTypedArray())
        val groundFluent = Fluent.positive(predicate, *pickA.args.toTypedArray())

        QuestionRemoveOperator(
            Problems.stackAB,
            Plan.of(listOf(BlockWorldDomain.Operators.stackAB)),
            pickA,
        ).buildHypotheticalProblem().first() shouldBe Problem.of(
            Domain.of(
                Problems.stackAB.domain.name,
                mutableSetOf(predicate).also { it.addAll(Problems.stackAB.domain.predicates) },
                mutableSetOf(
                    Action.of(
                        name = BlockWorldDomain.Actions.pick.name + "^",
                        parameters = BlockWorldDomain.Actions.pick.parameters,
                        preconditions = BlockWorldDomain.Actions.pick.preconditions,
                        effects = mutableSetOf(Effect.negative(fluent))
                            .also { it.addAll(BlockWorldDomain.Actions.pick.effects) },
                    ),
                ).also { it.addAll(Problems.stackAB.domain.actions) }.also { it.remove(BlockWorldDomain.Actions.pick) },
                Problems.stackAB.domain.types,
            ),
            Problems.stackAB.objects,
            State.of(mutableSetOf(groundFluent).also { it.addAll(Problems.stackAB.initialState.fluents) }),
            FluentBasedGoal.of(
                (Problems.stackAB.goal as FluentBasedGoal).targets.toMutableSet()
                    .also { it.add(groundFluent) },
            ),
        )
    }
}
