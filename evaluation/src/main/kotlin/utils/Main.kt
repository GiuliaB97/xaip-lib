package utils

import BaseBenchmark
import createPlansList
import domain.BlockWorldDomain
import domain.LogisticDomain
import java.lang.Exception

/**
 * Utility method to build the benchmarks.
 */
@Suppress("NestedBlockDepth")
fun main(args: Array<String>) {
    val problems = listOf(BlockWorldDomain.Problems.stackAB, LogisticDomain.Problems.robotFromLoc1ToLoc2)
    val maxLength = if (args.isNotEmpty()) args[0].toInt() else 50
    val isWorkflow = args.isNotEmpty()
    val explanationTypes = listOf("C", "")
    fun buildBenchmark() {
        val b = BaseBenchmark()
        for (problem in problems) {
            val plans = createPlansList(problem, maxLength)
            println("${problem.domain.name}: plans ${plans.size}")
            for (j in explanationTypes) {
                for (i in 1..5) {
                    try {
                        b.writeBenchmark("", problem, j, i, plans, isWorkflow)
                    } catch (_: Exception) { }
                }
            }
        }
    }

    buildBenchmark()
}
