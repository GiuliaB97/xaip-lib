package explanation.impl

import core.Fluent
import core.FluentBasedGoal
import core.Operator
import core.utility.then
import explanation.Explanation
import explanation.ExplanationPresenter

/**
 * Reification of the [Explanation] interface.
 */
internal open class BaseExplanationPresenter(
    override val explanation: Explanation,
) : ExplanationPresenter {
    private val minimalSolution by lazy { explanation.explainer.minimalPlanSelector(explanation.question.problem) }

    private fun beVerb(operators: List<Operator>) = (operators.size > 1).then("are") ?: "is"

    private val operator by lazy { (additionalOperators.size > 1).then("operators") ?: "operator" }
    private fun not(validCondition: Boolean) = (!validCondition).then("not ") ?: ""

    private val additionalOperators by lazy {
        explanation.novelPlan.operators.filter { !minimalSolution.operators.contains(it) }
    }

    private val operatorsMissing by lazy {
        minimalSolution.operators.filter { !explanation.novelPlan.operators.contains(it) }
    }

    private val isProposedPlanMinimalPlan = ((additionalOperators.isEmpty()) then true) ?: false

    private val isProblemSolvable by lazy {
        "The problem ${(explanation.question.problem.goal as FluentBasedGoal).targets} " +
            "is ${not(explanation.isProblemSolvable())}solvable."
    }

    private val originalPlan by lazy {
        "\nThe former plan was: ${explanation.originalPlan.operators}"
    }

    private val minimalPlan by lazy { "The minimal solution is: ${minimalSolution.operators}\n" }

    private val isPlanMinimalSolution by lazy { "The plan is ${not(isProposedPlanMinimalPlan)}the minimal solution" }

    private val areThereAdditionalOperators by lazy {
        (additionalOperators.isNotEmpty()).then(
            " There ${beVerb(additionalOperators)} ${additionalOperators.size} additional $operator " +
                "with respect to the minimal solution: $additionalOperators.\n",
        ) ?: ""
    }

    private val planContainsRequiredOperators by lazy {
        (!explanation.novelPlan.operators.containsAll(minimalSolution.operators)).then(
            " and it does not contains all the necessary operators.\n" +
                "There ${beVerb(operatorsMissing)} ${operatorsMissing.size} " +
                "$operator missing: $operatorsMissing.\n",
        ) ?: "."
    }

    private val isPlanValid by lazy {
        "\nThe novel plan is: ${explanation.novelPlan.operators}." +
            "\nThe novel plan is ${not(!explanation.isPlanValid())}a valid solution for the problem.\n"
    }

    private fun areAllGroundFluents(targets: Set<Fluent>) = targets.all { it.isGround }

    override fun present(): String {
        if (!areAllGroundFluents((explanation.question.problem.goal as FluentBasedGoal).targets)) {
            throw IllegalArgumentException("Goal must contain only ground fluents")
        }
        var finalString = isProblemSolvable
        if (explanation.isProblemSolvable()) {
            finalString = finalString.plus(originalPlan)
            finalString = finalString.plus(isPlanValid)
            finalString = finalString.plus(minimalPlan)

            finalString = finalString.plus(isPlanMinimalSolution)
            finalString = finalString.plus(planContainsRequiredOperators)
            finalString = finalString.plus(areThereAdditionalOperators)
        }
        return finalString
    }

    /**
     *
     */
    override fun presentMinimalExplanation(): String {
        return """Minimal explanation:
            | The plan: ${explanation.novelPlan.operators}, is valid: ${!explanation.isPlanValid()}
            | The length is acceptable: ${explanation.isPlanLengthAcceptable()}
            | Operators missing: $operatorsMissing
            | Additional operators: $additionalOperators
            |
            
        """.trimMargin()
    }
}
