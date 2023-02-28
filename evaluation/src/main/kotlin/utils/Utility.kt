import core.Operator
import core.Plan
import core.Planner
import core.Problem
import core.utility.then
import domain.BlockWorldDomain.Operators.pickA
import domain.BlockWorldDomain.Operators.pickB
import domain.BlockWorldDomain.Operators.pickC
import domain.BlockWorldDomain.Operators.pickD
import domain.BlockWorldDomain.Operators.putdownA
import domain.BlockWorldDomain.Operators.putdownB
import domain.BlockWorldDomain.Operators.putdownC
import domain.BlockWorldDomain.Operators.putdownD
import domain.BlockWorldDomain.Operators.stackAB
import domain.BlockWorldDomain.Operators.stackAC
import domain.BlockWorldDomain.Operators.stackAD
import domain.BlockWorldDomain.Operators.stackBA
import domain.BlockWorldDomain.Operators.stackBC
import domain.BlockWorldDomain.Operators.stackBD
import domain.BlockWorldDomain.Operators.stackCA
import domain.BlockWorldDomain.Operators.stackCB
import domain.BlockWorldDomain.Operators.stackCD
import domain.BlockWorldDomain.Operators.stackDA
import domain.BlockWorldDomain.Operators.stackDB
import domain.BlockWorldDomain.Operators.stackDC
import domain.BlockWorldDomain.Operators.unstackAB
import domain.BlockWorldDomain.Operators.unstackAC
import domain.BlockWorldDomain.Operators.unstackAD
import domain.BlockWorldDomain.Operators.unstackBA
import domain.BlockWorldDomain.Operators.unstackBC
import domain.BlockWorldDomain.Operators.unstackBD
import domain.BlockWorldDomain.Operators.unstackCA
import domain.BlockWorldDomain.Operators.unstackCB
import domain.BlockWorldDomain.Operators.unstackCD
import domain.BlockWorldDomain.Operators.unstackDA
import domain.BlockWorldDomain.Operators.unstackDB
import domain.BlockWorldDomain.Operators.unstackDC
import domain.LogisticsDomain.Operators.loadC1fromL2onR
import domain.LogisticsDomain.Operators.loadC2fromL3onR
import domain.LogisticsDomain.Operators.moveRfromL1toL2
import domain.LogisticsDomain.Operators.moveRfromL1toL3
import domain.LogisticsDomain.Operators.moveRfromL1toL5
import domain.LogisticsDomain.Operators.moveRfromL2toL1
import domain.LogisticsDomain.Operators.moveRfromL3toL1
import domain.LogisticsDomain.Operators.moveRfromL5toL1
import domain.LogisticsDomain.Operators.unloadC1fromRtoL1
import domain.LogisticsDomain.Operators.unloadC2fromRtoL1
import explanation.ContrastiveExplanationPresenter
import explanation.Explainer
import explanation.Question
import utils.MemorySampler
import java.io.File
import java.lang.management.ManagementFactory

/**
 * Method responsible for measuring the time required to calculate an [Explanation] for a [Question].
 */
fun measureTimeMillis(question: Question, explanationType: String): Long {
    val start = System.currentTimeMillis()
    if (explanationType.startsWith("c", true)) {
        ContrastiveExplanationPresenter.of(
            Explainer.of(Planner.strips()).explain(question),
        ).presentContrastiveExplanation()
    } else {
        ContrastiveExplanationPresenter.of(
            Explainer.of(Planner.strips()).explain(question),
        ).present()
    }
    return System.currentTimeMillis() - start
}

/**
 * Method responsible for measuring the memory (heap) required to calculate an [Explanation] for a [Question].
 */
fun measureMemory(question: Question, explanationType: String): Long {
    val mbean = ManagementFactory.getMemoryMXBean()
    val beforeHeapMemoryUsage = mbean.heapMemoryUsage
    if (explanationType.startsWith("c", true)) {
        val instance = ContrastiveExplanationPresenter.of(
            Explainer.of(Planner.strips()).explain(question),
        ).presentContrastiveExplanation()
    } else {
        ContrastiveExplanationPresenter.of(Explainer.of(Planner.strips()).explain(question)).present()
    }
    val afterHeapMemoryUsage = mbean.heapMemoryUsage
    val result = afterHeapMemoryUsage.used - beforeHeapMemoryUsage.used
    return (result > 0).then(result) ?: 0
}

/**
 * Method responsible for measuring the memory required to calculate an [Explanation] for a [Question].
*/
fun measureMemory2(question: Question, explanationType: String): Long {
    val sampler = MemorySampler(0)

    sampler.start()

    if (explanationType.startsWith("c", true)) {
        val instance = ContrastiveExplanationPresenter.of(
            Explainer.of(Planner.strips()).explain(question),
        ).presentContrastiveExplanation()
    } else {
        ContrastiveExplanationPresenter.of(
            Explainer.of(Planner.strips()).explain(question),
        ).present()
    }

    return sampler.terminate()
//        .also { println("Memory benchmark: $it") }
        .max
}

/**
 * Method responsible for creating a list of plans for a given [problem].
 */
fun createPlansList(problem: Problem, maxLength: Int): List<Plan> {
    if (problem.domain.name == "block_world") {
        val idempotentActionSetBlockWorld = listOf(
            listOf(pickA, putdownA),
            listOf(pickB, putdownB),
            listOf(pickC, putdownC),
            listOf(pickD, putdownD),
            listOf(pickA, stackAB, unstackAB),
            listOf(pickA, stackAC, unstackAC),
            listOf(pickA, stackAD, unstackAD),
            listOf(pickB, stackBA, unstackBA),
            listOf(pickB, stackBC, unstackBC),
            listOf(pickB, stackBD, unstackBD),
            listOf(pickC, stackCA, unstackCA),
            listOf(pickC, stackCB, unstackCB),
            listOf(pickC, stackCD, unstackCD),
            listOf(pickD, stackDA, unstackDA),
            listOf(pickD, stackDB, unstackDB),
            listOf(pickD, stackDC, unstackDC),
        )
        val plans = mutableListOf(Plan.of(listOf(pickA, stackAB)))
        return create(maxLength, plans, idempotentActionSetBlockWorld)
    } else {
        val idempotentActionSetLogistic = listOf(
            listOf(moveRfromL1toL2, moveRfromL2toL1),
            listOf(moveRfromL1toL3, moveRfromL3toL1),
            listOf(moveRfromL1toL5, moveRfromL5toL1),
            listOf(moveRfromL1toL3, loadC2fromL3onR, moveRfromL3toL1, unloadC2fromRtoL1),
            listOf(moveRfromL1toL2, loadC1fromL2onR, moveRfromL2toL1, unloadC1fromRtoL1),
        )
        val plans = mutableListOf(Plan.of(listOf(moveRfromL1toL2)))
        return create(maxLength, plans, idempotentActionSetLogistic)
    }
}

@Suppress("NestedBlockDepth")
private fun create(
    maxLength: Int,
    plans: MutableList<Plan>,
    idempotentActionSetBlockWorld: List<List<Operator>>,
): List<Plan> {
    val iterator = plans.listIterator()
    val concatenationForPlan = 2
    while (true) {
        val elem = plans.random()
        val tmp = mutableListOf<List<Operator>>()
        for (i in 1..concatenationForPlan) {
            val seq = idempotentActionSetBlockWorld.random()
            val list = mutableListOf<Operator>()
            list.addAll(seq)
            list.addAll(elem.operators)
            tmp.add(list)
            if (list.size > maxLength) return plans.toList().distinct()
        }
        for (l in tmp) iterator.add(Plan.of(l))
    }
}

/**
 * Method responsible for deleting all the files in a given directory.
 */
fun cleanDirectory(name: String) {
    val directory = File(name)
    directory.listFiles()
        ?.filterNot { it.isDirectory }
        ?.forEach { it.delete() }
}
