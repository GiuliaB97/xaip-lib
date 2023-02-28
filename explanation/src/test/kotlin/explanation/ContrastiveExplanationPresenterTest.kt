package explanation

import core.Plan
import core.Planner
import domain.BlockWorldDomain
import domain.BlockWorldDomain.Operators.pickA
import domain.BlockWorldDomain.Operators.pickC
import domain.BlockWorldDomain.Operators.putdownC
import domain.BlockWorldDomain.Operators.stackAB
import domain.BlockWorldDomain.Problems
import domain.LogisticsDomain
import explanation.impl.QuestionAddOperator
import explanation.impl.QuestionPlanProposal
import explanation.impl.QuestionPlanSatisfiability
import explanation.impl.QuestionRemoveOperator
import explanation.impl.QuestionReplaceOperator
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.string.shouldStartWith

class ContrastiveExplanationPresenterTest : AnnotationSpec() {

    private fun testExplanation(question: Question) {
        val explanation = Explainer.of(Planner.strips()).explain(question)
        val explanationPresenter = ContrastiveExplanationPresenter.of(explanation)
        explanationPresenter.present() shouldStartWith ("The")
        explanationPresenter.presentMinimalExplanation() shouldStartWith ("Minimal")
        explanationPresenter.presentContrastiveExplanation() shouldStartWith ("Contrastive")
    }

    // Add operator
    @Test
    fun `Add useless operator (pickA) to the plan pickC in pickC problem (incorrect plan)`() {
        val question = QuestionAddOperator(
            Problems.pickC,
            Plan.of(listOf(pickC)),
            pickA,
            0,
        )
        testExplanation(question)
    }

    @Test
    fun `Basic Add useless operator (pickA) to the plan pickC in pickC problem (incorrect plan)`() {
        val question = QuestionAddOperator(
            LogisticsDomain.Problems.basicRobotFromLocation1ToLocation2,
            Plan.of(
                listOf(
                    LogisticsDomain.Operators.moveRfromL1toL2,
                    LogisticsDomain.Operators.moveRfromL2toL1,
                ),
            ),
            LogisticsDomain.Operators.moveRfromL2toL1,
            1,
        )
        testExplanation(question)
    }

    @Test
    fun `Add pickA to the plan stackAB in stackAB problem (correct plan)`() {
        val question = QuestionAddOperator(
            Problems.stackAB,
            Plan.of(listOf(stackAB)),
            pickA,
            0,
        )
        testExplanation(question)
    }

    // Remove operator
    @Test
    fun `Remove pickA from the plan to solve the armNotEmpty problem`() {
        val question = QuestionRemoveOperator(
            Problems.pickC,
            Plan.of(listOf(pickA, pickC)),
            pickA,
        )
        testExplanation(question)
    }

    // Replace operator

    @Test
    fun `Replace pickA with pickC in stackAB problem`() {
        val question = QuestionReplaceOperator(
            Problems.stackAB,
            Plan.of(listOf(pickC, stackAB)),
            pickA,
            0,
            BlockWorldDomain.States.allBlocksAtFloor,
        )
        testExplanation(question)
    }

    // Plan proposal

    @Test
    fun `BlockWorld domain test incorrect plan`() {
        val question = QuestionPlanProposal(
            Problems.stackAB,
            Plan.of(listOf(pickA, stackAB)),
            Plan.of(listOf(pickA)),
        )
        testExplanation(question)
    }

    // Plan satisfiability

    @Test
    fun `BlockWorld plan valid`() {
        val question = QuestionPlanSatisfiability(Problems.pickC, Plan.of(listOf(pickC)))
        testExplanation(question)
    }

    @Test
    fun `BlockWorld plan not valid`() { // idempotent operators
        val question = QuestionPlanSatisfiability(
            Problems.pickC,
            Plan.of(listOf(pickC, putdownC)),
        )
        testExplanation(question)
    }

    @Test
    fun `Test exception on not ground goal`() {
        val q1 = QuestionAddOperator(
            Problems.armNotEmpty,
            Plan.of(listOf(BlockWorldDomain.Operators.pickB)),
            pickC,
            1,
        )

        val explanation = Explainer.of(Planner.strips()).explain(q1)
        val exception = shouldThrow<IllegalArgumentException> {
            ContrastiveExplanationPresenter.of(explanation).present()
        }
        exception.message shouldStartWith ("Goal must contain only ground fluents")
    }
}
