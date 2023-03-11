/***
 * .
 */
import core.*
import domain.BlockWorldDomain as BlockWorld
import domain.LogisticsDomain as Logistics

class Controller {
    companion object {
        private const val DEBUG = false

        private fun log(msg: () -> String) {
            if (DEBUG) {
                println(msg())
                System.out.flush()
            }
        }
    }
    private lateinit var problem: Problem
    private lateinit var arguments: List<String>
    fun checkQuestion(
        domainName: CharSequence?,
        problemName: CharSequence?,
        formerPlanTextField: CharSequence?,
        questionType: CharSequence?,
        actionName: CharSequence?,
        actionPosition: Int,
        alternativePlan: CharSequence?,
        explanationType: CharSequence?,
    ) {
        log { "checkQuestion: $domainName" }
        when (questionType) {
            "Question 1" ->
                if (
                    formerPlanTextField!!.isEmpty() ||
                    actionName!!.isEmpty() ||
                    formerPlanTextField.isEmpty()
                ) {
                    error("checkQuestion: Question 1: missing fields")
                } else {
                    log { "checkQuestion: Question 1" }
                    /*QuestionRemoveOperator(

                    )

                     */
                }
            "Question 2" -> if (
                formerPlanTextField!!.isEmpty() ||
                actionName!!.isEmpty() ||
                formerPlanTextField.isEmpty()
            ) {
                error("checkQuestion: Question 2: missing fields")
            } else {
                log { "checkQuestion: Question 2" }
            }
            "Question 3" -> if (
                formerPlanTextField!!.isEmpty() ||
                actionName!!.isEmpty() ||
                formerPlanTextField.isEmpty()
            ) {
                error("checkQuestion: Question 3: missing fields")
            } else {
                log { "checkQuestion: Question 3" }
            }
            "Question 4" -> if (
                formerPlanTextField!!.isEmpty() ||
                actionName!!.isEmpty() ||
                formerPlanTextField.isEmpty()
            ) {
                error("checkQuestion: Question 4: missing fields")
            } else {
                log { "checkQuestion: Question 4" }
            }
            "Question 5" -> log { "checkQuestion: Question 5" }

            else -> error("Question not recognized")
        }
        problem = getProblem(domainName.toString(), problemName.toString())
    }

    /**
     * Method responsible for retrieving the list of the problems corresponding to input [Domain].
     */
    fun getProblems(domain: String): List<Problem> {
        return if (domain.startsWith("b").or(domain.startsWith("B"))) {
            BlockWorld.problems.toList()
        } else {
            Logistics.problems.toList()
        }
    }

    private fun getProblem(domainName: String, problemName: String) =
        getProblems(domainName).first { it.name == problemName }

    fun getAction(operator: String): Action {
        log { "getAction: string operator: $operator" }
        val operatorName = problem.domain.actions.first { act -> act.name == operator.dropLastWhile { it == '(' } }
        log { "getAction: operator name from input string: $operatorName" }
        val init = operator.indexOf("(")
        log { "getAction: index of (: $init" }
        val arguments = operator.drop(init).filter { it.isLetterOrDigit() }.toList()
        log { "operators: $arguments \n actionName: $operatorName" }
        val operatorNameMatched = problem.domain.actions.first { it.name == operator }
        log { "getAction: operatorNameMatched: $operatorNameMatched" }
        return operatorNameMatched
    }

    fun getParameterType(parameterName: String): Any =
        problem.objects.map.forEach { (type, objectSet) ->
            if (
                objectSet.any { it.representation == parameterName }
            ) {
                log { "getParameterType: type: $type" }
                return type
            }
        }

    private fun getOperator(action: Action): Operator {
        val operator = Operator.of(action)
        log { "getOperator: operator $operator" }
        for (arg in arguments) {
            val entry = action.parameters.toList()[arguments.indexOf(arg)]
            val substitution = VariableAssignment.of(Variable.of(entry.first.name), Object.of(arg))
            operator.apply(substitution)
            log { "getOperator: entry $entry \t substitution $substitution" }
        }
        log { "getOperator: operator: $operator" }
        return operator
    }

    fun getPlan(plan: String): Plan {
        val init = plan.indexOf("[")
        val last = plan.indexOf("]")
        log { "getPlan: indexes $init $last \t string plan:" }
        val arguments = plan.drop(init).dropLast(last)
        log { "getPlan: arguments $arguments" }
        val index = 0
        val list = mutableListOf<Operator>()
        for (letter in arguments) {
            if (letter == ',') {
                list.add(getOperator(getAction(arguments.drop(index).dropLast(arguments.indexOf(letter)))))
                log { "getPlan: indexes $init $last" }
            }
        }
        log { "getPlan: list $list" }
        return Plan.of(list)
    }
}
