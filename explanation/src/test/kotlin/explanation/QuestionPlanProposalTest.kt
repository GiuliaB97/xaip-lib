package explanation

import core.Plan
import core.Planner
import domain.BlockWorldDomain.Operators
import domain.LogisticsDomain.Operators.moveRfromL1toL2
import domain.LogisticsDomain.Operators.moveRfromL1toL5
import explanation.impl.QuestionPlanProposal
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import domain.BlockWorldDomain.Problems as BlockWorldProblem
import domain.LogisticsDomain.Problems as GraphProblem

class QuestionPlanProposalTest : AnnotationSpec() {
    private val planPickAStackAB = Plan.of(listOf(Operators.pickA, Operators.stackAB))
    private val planPickA = Plan.of(listOf(Operators.pickA))
    private val planPickB = Plan.of(listOf(Operators.pickB))
    private val planPickBstackBApickC = Plan.of(listOf(Operators.pickB, Operators.stackBA, Operators.pickC))

    private val graphProblemRtoX = GraphProblem.rToX
    private val graphProblemRfromLoc1Loc2 = GraphProblem.robotFromLoc1ToLoc2
    private val planRfromL1toL2 = Plan.of(listOf(moveRfromL1toL2))
    private val planRfromL1toL5 = Plan.of(listOf(moveRfromL1toL5))

    @Test
    fun `BlockWorld domain test valid plan`() {
        val q4 = QuestionPlanProposal(
            BlockWorldProblem.armNotEmpty,
            planPickBstackBApickC,
            planPickB,
        )

        val explanation = Explainer.of(Planner.strips()).explain(q4)
        explanation.isPlanValid() shouldBe true
    }

    @Test
    fun `BlockWorld domain test incorrect plan`() {
        val q4 = QuestionPlanProposal(
            BlockWorldProblem.stackAB,
            planPickAStackAB,
            planPickA,
        )
        val explanation = Explainer.of(Planner.strips()).explain(q4)
        explanation.isPlanValid() shouldBe false
    }

    @Test
    fun `Graph domain test valid plan`() {
        val q4 = QuestionPlanProposal(
            graphProblemRtoX,
            planRfromL1toL2,
            planRfromL1toL5,
        )
        val explanation = Explainer.of(Planner.strips()).explain(q4)
        explanation.isPlanValid() shouldBe true
    }

    @Test
    fun `Graph domain test incorrect plan`() {
        val q4 = QuestionPlanProposal(
            graphProblemRfromLoc1Loc2,
            planRfromL1toL2,
            planRfromL1toL5,
        )
        val explanation = Explainer.of(Planner.strips()).explain(q4)
        println(explanation.novelPlan)
        explanation.isPlanValid() shouldBe false
    }

    @Test
    fun `Hypothetical domain exception`() {
        val exception = shouldThrow<UnsupportedOperationException> {
            QuestionPlanProposal(
                graphProblemRfromLoc1Loc2,
                planRfromL1toL2,
                planRfromL1toL5,
            ).buildHypotheticalDomain()
        }
        exception.message shouldStartWith "Reconciliation"
    }

    @Test
    fun `Hypothetical problem exception`() {
        val exception = shouldThrow<UnsupportedOperationException> {
            QuestionPlanProposal(
                graphProblemRfromLoc1Loc2,
                planRfromL1toL2,
                planRfromL1toL5,
            ).buildHypotheticalProblem()
        }
        exception.message shouldStartWith "Reconciliation"
    }
}
