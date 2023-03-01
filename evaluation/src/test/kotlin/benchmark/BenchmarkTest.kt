package benchmark

import BaseBenchmark
import cleanDirectory
import core.Planner
import createPlansList
import domain.BlockWorldDomain.Problems.stackAB
import domain.LogisticsDomain.Problems.robotFromLoc1ToLoc2
import explanation.Explainer
import explanation.impl.QuestionPlanSatisfiability
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import java.io.File

class BenchmarkTest : AnnotationSpec() {
    private val problems = listOf(stackAB, robotFromLoc1ToLoc2)
    private val maxLength = 2
    private val explanationTypes = listOf("C", "")

    @Test
    fun createOnlyValidPlansBlockWorldDomainTest() {
        val plans = createPlansList(problems.first(), maxLength)
        plans.all {
            !Explainer.of(Planner.strips()).explain(QuestionPlanSatisfiability(problems.first(), it)).isPlanValid()
        } shouldBe false
    }

    @Test
    fun createOnlyValidLogisticDomainTest() {
        val plans = createPlansList(problems.last(), maxLength)
        plans.all {
            !Explainer.of(Planner.strips()).explain(QuestionPlanSatisfiability(problems.first(), it)).isPlanValid()
        } shouldBe true
    }

    @Test
    fun buildBenchmarkTest() {
        val benchmark = BaseBenchmark()
        for (problem in problems) {
            val plans = createPlansList(problem, maxLength)
            for (j in explanationTypes)
                for (i in 1..5) {
                    benchmark.writeBenchmark("Test$i$j", problem, j, i, plans)
                    val file = File("res/Test$i$j")
                    file.exists() shouldBe true
                }
        }
        cleanDirectory("res")
    }
}
