import core.Plan
import core.Planner
import core.Problem
import core.utility.then
import domain.BlockWorldDomain.Actions
import domain.BlockWorldDomain.Operators.pickA
import domain.LogisticsDomain.Operators.moveRfromL1toL2
import explanation.Explainer
import explanation.Question
import explanation.impl.* // ktlint-disable no-wildcard-imports
import explanation.utils.retrieveAction
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.random.Random
import domain.LogisticsDomain.Actions as Lactions

open class BaseBenchmark {
    private val explainer by lazy { Explainer.of(Planner.strips()) }

    private val blockWorldName by lazy { "block_world" }

    private val logisticsName by lazy { "logistics" }

    private val resultsTime by lazy { mutableMapOf<Plan, Long>() }
    private val resultsMemory by lazy { mutableMapOf<Plan, Long>() }
    private fun domainName(name: String) = (name == blockWorldName).then(blockWorldName) ?: logisticsName
    private fun explanationType(name: String) = (name.startsWith("c", true))
        .then("contrastiveExplanation") ?: "generalExplanation"

    private fun osName(name: String) = (name.startsWith("l", true)).then("linux") ?: "windows"

    /**
     * Method responsible for writing the benchmarks.
     */
    fun writeBenchmark(
        filename: String,
        problem: Problem,
        explanationType: String,
        questionType: Int,
        plans: List<Plan>,
    ) {
        init(plans.toMutableList(), questionType, problem, explanationType)
        write(filename, problem, explanationType, questionType)
    }
    private fun write(
        filename: String,
        problem: Problem,
        explanationType: String,
        questionType: Int
    ) {
        fun OutputStream.writeCsv() {
            val writer = bufferedWriter()
            writer.write("""Domain, PlanLength, QuestionType, Time, Memory""".trimMargin())
            writer.newLine()
            var i = 1
            resultsTime.forEach {
                writer.write(
                    "${problem.domain.name}, ${it.key.operators.size}, $questionType, " +
                        "${it.value}, ${resultsMemory[it.key]}",
                )
                writer.newLine()
                i++
            }
            writer.flush()
        }
        val prefix = "" // You run it from the ide you need to add this prefix: "evaluation/"
        (
            (filename == "").then(
                """${prefix}res/benchmark/
                    ${domainName(problem.domain.name)}_
                    ${osName(System.getProperty("os.name"))}_
                    ${explanationType(explanationType)}_
                    Question$questionType.csv
                """.replace("\\s".toRegex(), ""),
            ) ?: "res/$filename"
            ).let {
            FileOutputStream(
                it,
            ).apply { writeCsv() }
        }
    }

    private fun addResult(question: Question, explanationType: String) {
        val memoryOccupation = measureMemory2(question, explanationType)
        val time = when (question) {
            is QuestionReplaceOperator ->
                measureTimeMillis(
                    QuestionReplaceOperator(
                        question.problem,
                        question.plan,
                        question.focus,
                        question.focusOn,
                        question.inState,
                    ),
                    explanationType,
                )
            else ->
                measureTimeMillis(question, explanationType)
        }

        if (memoryOccupation > 0L && time > 0) {
            resultsMemory[question.plan] = memoryOccupation
            resultsTime[question.plan] = time
        }
    }

    private fun init(plans: MutableList<Plan>, question: Int, problem: Problem, explanationType: String = "") {
        for (plan in plans) {
            when (question) {
                1 -> explainQuestion1(plan, problem, explanationType)
                2 -> explainQuestion2(plan, problem, explanationType)
                3 -> explainQuestion3(plan, problem, explanationType)
                4 -> explainQuestion4(plan, problem, explanationType)
                5 -> explainQuestion5(plan, problem, explanationType)
                else -> error("Question $question is not supported")
            }
        }
    }

    private fun explainQuestion1(plan: Plan, problem: Problem, type: String) {
        val actions = setOf(Actions.pick, Actions.stack, Lactions.move)
        for (operator in plan.operators)
            if (operator !in explainer.minimalPlanSelector(problem).operators &&
                problem.domain.actions.retrieveAction(operator) in actions
            ) {
                log { "question: $type plan: $plan operator: $operator" }
                addResult(QuestionRemoveOperator(problem, plan, operator), type)
            }
    }

    private fun explainQuestion2(plan: Plan, problem: Problem, type: String) {
        for (i in 1..plan.operators.size) {
            val operator = plan.operators.shuffled(Random(i)).first()
            log { "question: $type plan: $plan operator: $operator" }
            addResult(QuestionAddOperator(problem, plan, operator, i), type)
        }
    }

    private fun explainQuestion3(plan: Plan, problem: Problem, type: String) {
        val actions = setOf(Actions.putdown, Actions.unstack, Lactions.unload)
        val op = (problem.domain.name == "block_world").then(pickA) ?: moveRfromL1toL2
        val iterator = plan.operators.iterator()
        var i = 0
        for (operator in iterator) {
            if (problem.domain.actions.retrieveAction(operator) in actions &&
                plan.operators.last() != operator
            ) {
                try {
                    log { "question: $type plan: $plan operator: $op" }
                    addResult(QuestionReplaceOperator(problem, plan, op, i + 1), type)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            i++
        }
    }

    private fun explainQuestion4(plan: Plan, problem: Problem, type: String) {
        val plans = mutableSetOf<Plan>()
        log { "question: $type plan: $plan" }
        for (i in 1..plan.operators.size / 2) {
            plans.add(Plan.of(plan.operators.shuffled(Random(i))))
        }
        for (p in plans) {
            addResult(QuestionPlanProposal(problem, plan, p), type)
        }
    }

    private fun explainQuestion5(plan: Plan, problem: Problem, type: String) {
        log { "question: $type plan: $plan" }
        addResult(QuestionPlanSatisfiability(problem, plan), type)
    }

    companion object {
        private const val DEBUG = false

        private fun log(msg: () -> String) {
            if (DEBUG) {
                println(msg())
                System.out.flush()
            }
        }
    }
}
