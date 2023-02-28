package explanation

import core.Plan
import core.Planner
import core.State
import domain.BlockWorldDomain.Fluents
import domain.BlockWorldDomain.Operators.pickA
import domain.BlockWorldDomain.Operators.pickC
import domain.BlockWorldDomain.Operators.pickD
import domain.BlockWorldDomain.Operators.stackAB
import domain.BlockWorldDomain.Operators.stackAC
import domain.BlockWorldDomain.Problems
import domain.BlockWorldDomain.States
import domain.LogisticsDomain.Operators.moveRfromL1toL2
import domain.LogisticsDomain.Operators.moveRfromL1toL3
import explanation.impl.QuestionReplaceOperator
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import domain.LogisticsDomain.Problems as GraphProblem

class QuestionReplaceOperatorTest : AnnotationSpec() {
    @Test
    fun `Replace stackAB with stackAC in stackZWpickX problem`() {
        val q3 = QuestionReplaceOperator(
            Problems.stackZWpickX,
            Plan.of(listOf(pickA, stackAB, pickD)),
            stackAC,
            1,
            State.of(
                Fluents.atAArm,
                Fluents.clearA,
                Fluents.atCFloor,
                Fluents.clearC,
                Fluents.atDFloor,
                Fluents.clearD,
                Fluents.clearB,
                Fluents.atBFloor,
                Fluents.armEmpty,
            ),
        )
        val explanation = Explainer.of(Planner.strips()).explain(q3)

        explanation.originalPlan shouldBe q3.plan
        explanation.novelPlan shouldBe Plan.of(listOf(pickA, stackAC, pickD))
        explanation.addedList shouldBe listOf(stackAC)
        explanation.deletedList shouldBe listOf(stackAB)
        explanation.sharedList shouldBe listOf(pickA, pickD)
        explanation.isPlanValid() shouldBe true
    }

    @Test
    fun `Replace pickC with pickD in stackZWpickX problem`() {
        val q3 = QuestionReplaceOperator(
            Problems.stackZWpickX,
            Plan.of(listOf(pickA, stackAB, pickC)),
            pickD,
            2,
            State.of(
                Fluents.onAB,
                Fluents.clearA,
                Fluents.atCFloor,
                Fluents.clearC,
                Fluents.atDFloor,
                Fluents.clearD,
                Fluents.atBFloor,
                Fluents.armEmpty,
            ),
        )
        val explanation = Explainer.of(Planner.strips()).explain(q3)

        explanation.originalPlan shouldBe q3.plan
        explanation.novelPlan shouldBe Plan.of(listOf(pickA, stackAB, pickD))
        explanation.addedList shouldBe listOf(pickD)
        explanation.deletedList shouldBe listOf(pickC)
        explanation.sharedList shouldBe listOf(pickA, stackAB)
        explanation.isPlanValid() shouldBe true
    }

    @Test
    fun `Replace pickA with pickC in stackAB problem`() {
        val q3 = QuestionReplaceOperator(
            Problems.stackAB,
            Plan.of(listOf(pickC, stackAB)),
            pickA,
            0,
            States.allBlocksAtFloor,
        )
        val explanation = Explainer.of(Planner.strips()).explain(q3)

        explanation.originalPlan shouldBe q3.plan
        explanation.novelPlan shouldBe Plan.of(listOf(pickA, stackAB))
        explanation.addedList shouldBe listOf(pickA)
        explanation.deletedList shouldBe listOf(pickC)
        explanation.sharedList shouldBe listOf(stackAB)
        explanation.isPlanValid() shouldBe true
    }

    @Test
    fun `Replace moveRfromL1toL2 with moveRfromL1toL5 in the plan`() {
        val planRfromL1toL2 = Plan.of(listOf(moveRfromL1toL2))
        val graphProblemRtoX = GraphProblem.rToX
        val q3 = QuestionReplaceOperator(
            graphProblemRtoX,
            planRfromL1toL2,
            moveRfromL1toL3,
            0,
        )
        val explanation = Explainer.of(Planner.strips()).explain(q3)

        explanation.originalPlan shouldBe q3.plan
        explanation.novelPlan shouldBe Plan.of(listOf(moveRfromL1toL3))
        explanation.addedList shouldBe listOf(moveRfromL1toL3)
        explanation.deletedList shouldBe listOf(moveRfromL1toL2)
        explanation.sharedList shouldBe emptyList()
        explanation.isPlanValid() shouldBe true
    }

    @Test
    fun `Test exception`() {
        val exception = shouldThrow<IllegalArgumentException> {
            Explainer.of(Planner.strips()).explain(
                QuestionReplaceOperator(
                    Problems.stackZWpickX,
                    Plan.of(listOf(pickA, stackAB, pickC)),
                    stackAB,
                    0,
                ),
            )
        }
        exception.message shouldStartWith "Error"
    }
}
