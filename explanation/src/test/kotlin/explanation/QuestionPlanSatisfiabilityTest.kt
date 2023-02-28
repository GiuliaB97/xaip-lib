package explanation

import core.Plan
import core.Planner
import domain.BlockWorldDomain.Operators.pickA
import domain.BlockWorldDomain.Operators.putdownA
import domain.BlockWorldDomain.Problems
import domain.LogisticsDomain
import domain.LogisticsDomain.Operators.moveRfromL1toL2
import domain.LogisticsDomain.Operators.moveRfromL1toL5
import explanation.impl.QuestionPlanSatisfiability
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith

class QuestionPlanSatisfiabilityTest : AnnotationSpec() {
    private val graphProblemRfromLoc1Loc2 = LogisticsDomain.Problems.robotFromLoc1ToLoc2
    private val planRfromL1toL2 = Plan.of(listOf(moveRfromL1toL2))
    private val planRfromL1toL5 = Plan.of(listOf(moveRfromL1toL5))

    @Test
    fun `BlockWorld plan valid`() {
        val q5 = QuestionPlanSatisfiability(Problems.pickX, Plan.of(listOf(pickA)))
        val explanation = Explainer.of(Planner.strips()).explain(q5)
        println(explanation.toString())
        explanation.isPlanValid() shouldBe true
    }

    @Test
    fun `BlockWorld plan not valid`() { // idempotent operators
        val q5 = QuestionPlanSatisfiability(
            Problems.pickX,
            Plan.of(listOf(pickA, putdownA)),
        )
        val explanation = Explainer.of(Planner.strips()).explain(q5)
        explanation.isPlanValid() shouldBe false
    }

    @Test
    fun `GraphDomain plan valid`() {
        val q5 = QuestionPlanSatisfiability(
            graphProblemRfromLoc1Loc2,
            planRfromL1toL2,
        )
        val explanation = Explainer.of(Planner.strips()).explain(q5)
        println(explanation.toString())
        explanation.isPlanValid() shouldBe true
    }

    @Test
    fun `GraphDomain plan not valid`() {
        val q5 = QuestionPlanSatisfiability(
            graphProblemRfromLoc1Loc2,
            planRfromL1toL5,
        )
        val explanation = Explainer.of(Planner.strips()).explain(q5)
        println(explanation.toString())
        explanation.isPlanValid() shouldBe false
    }

    @Test
    fun `Hypothetical domain exception`() {
        val exception = shouldThrow<UnsupportedOperationException> {
            QuestionPlanSatisfiability(
                Problems.pickX,
                Plan.of(listOf(pickA, putdownA)),
            ).buildHypotheticalDomain()
        }
        exception.message shouldStartWith "Reconciliation"
    }

    @Test
    fun `Hypothetical problem exception`() {
        val exception = shouldThrow<UnsupportedOperationException> {
            QuestionPlanSatisfiability(
                Problems.pickX,
                Plan.of(listOf(pickA, putdownA)),
            ).buildHypotheticalProblem()
        }
        exception.message shouldStartWith "Reconciliation"
    }
}
