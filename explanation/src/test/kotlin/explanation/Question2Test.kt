package explanation

import Plan
import explanation.impl.Question2
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import domain.BlockWorldDomain.Operators.pickA
import domain.BlockWorldDomain.Operators.pickB
import domain.BlockWorldDomain.Planners.stripsPlanner
import domain.BlockWorldDomain.Problems
class Question2Test : AnnotationSpec() {
    @Test
    fun `Remove pickA from the plan to solve the armNotEmpty problem`() {
        val q2 = Question2(
            Problems.armNotEmpty,
            Plan.of(listOf(pickA)),
            pickA,
            0
        )

        val hplan = stripsPlanner.plan(q2.buildHproblem()).first()
        val explanation = Explanation.of(q2.plan, hplan, q2)

        val contrastiveExplanation = Explanation.of(
            q2.plan,
            Plan.of(listOf(pickB)),
            q2
        )
        explanation shouldBe contrastiveExplanation
        explanation.isPlanValid() shouldBe true
    }
}
