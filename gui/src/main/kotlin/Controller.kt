/***
 * .
 */
import core.* // ktlint-disable no-wildcard-imports
import explanation.Explainer
import explanation.ExplanationPresenter
import explanation.Question
import explanation.impl.*
import javafx.scene.control.ComboBox
import domain.BlockWorldDomain as BlockWorld
import domain.LogisticsDomain as Logistics

/**
 * TODO 1: fix on change value question; it seems like even if update the former value stays unchanged.
 * TODO 2: fix property when change question value.
 */
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
    private lateinit var parameters: List<String>
    private lateinit var action: Action
    private lateinit var plan: Plan
    private lateinit var alternativePlan: Plan
    private lateinit var operator: Operator
    private lateinit var question: Question
    private lateinit var view: View

    fun checkQuestion(
        viewRef: View,
        domainName: CharSequence?,
        problemName: CharSequence?,
        questionType: CharSequence?,
        formerPlanTextField: CharSequence?,
        actionName: CharSequence?,
        actionPosition: Int,
        alternativePlanTextField: CharSequence?,
        explanationType: CharSequence?,
        actionParametersComboBox: List<ComboBox<String>>,
        stateTextField: CharSequence,
    ) {
        view = viewRef
        println("checkQuestion: $domainName \t question: $questionType")
        log { "checkQuestion: $domainName" }
        if (
            formerPlanTextField!!.isEmpty() ||
            // actionName!!.isEmpty() ||
            formerPlanTextField.isEmpty()
        ) {
            error("checkQuestion: Question 1: missing fields")
        } else {
            problem = getProblem(domainName.toString(), problemName.toString())
            plan = getPlan(formerPlanTextField.toString())
            // if (parameters.isNotEmpty()) {
            action = getAction(actionName.toString())
            parameters = actionParametersComboBox.map { it.value }.filter { !it.isNullOrBlank() }
            log { "parameters: $parameters" }
            operator = getOperator(action, parameters)
            // }
        }
        if (!alternativePlanTextField.isNullOrBlank()) {
            alternativePlan = getPlan(alternativePlanTextField.toString())
        }
        when (questionType) {
            "Question 1" -> {
                log { "checkQuestion: Question 1" }
                question = QuestionRemoveOperator(
                    problem,
                    plan,
                    operator,
                )
                log { "question: $question" }
            }
            "Question 2" -> {
                log { "checkQuestion: Question 2" }
                question = QuestionAddOperator(
                    problem,
                    plan,
                    operator,
                    actionPosition,
                )
                log { "question: $question" }
            }
            "Question 3" -> {
                log { "checkQuestion: Question 3" }
                val state = getState(stateTextField.toString())
                question = QuestionReplaceOperator(
                    problem,
                    plan,
                    operator,
                    actionPosition,
                    state,
                )
                println("checkQuestion: Question 3: $question")
            }
            "Question 4" -> if (
                alternativePlanTextField!!.isEmpty()
            ) {
                error("checkQuestion: Question 4: missing fields")
            } else {
                log { "checkQuestion: Question 4" }
                question = QuestionPlanProposal(
                    problem,
                    plan,
                    alternativePlan,
                )
            }
            "Question 5" -> {
                log { "checkQuestion: Question 5" }
                question = QuestionPlanSatisfiability(
                    problem,
                    plan,
                )
            }

            else -> error("Question not recognized")
        }
        val explanation = Explainer.of(Planner.strips()).explain(question)
        val explanationPresenter = ExplanationPresenter.of(explanation)
        val explanationString = ""
        if (explanationType?.startsWith("General")!!) {
            explanationPresenter.present()
        } else {
            explanationPresenter.presentMinimalExplanation()
        }
        view.showExplanation(explanationString)
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

    private fun getAction(operatorName: String): Action {
        println("getAction: string operator: $operatorName")
        val actionMatched = problem.domain.actions.first { act -> act.name == operatorName }
        println("getAction: operator name from input string: $actionMatched")
        /*
        val init = operator.indexOf("(")
        println("getAction: index of (: $init")
        val arguments = operator.drop(init).filter { it.isLetterOrDigit() }.toList()
        log { "operators: $arguments \n actionName: $operatorName" }
        val operatorNameMatched = problem.domain.actions.first { it.name == operator }
        log { "getAction: operatorNameMatched: $operatorNameMatched" }
        return operatorNameMatched
         */
        return actionMatched
    }

    private fun getParameterType(parameterName: String): Any =
        problem.objects.map.forEach { (type, objectSet) ->
            if (
                objectSet.any { it.representation == parameterName }
            ) {
                log { "getParameterType: type: $type" }
                return type
            }
        }

    private fun getOperator(action: Action, parameters: List<String>): Operator {
        val operator = Operator.of(action)
        val variableList = listOf(Variable.of("X"), Variable.of("Y"), Variable.of("Z"))
        log { "getOperator: operator $operator" }
        for ((i, arg) in parameters.withIndex()) {
            println("arg: $arg")
            val substitution = VariableAssignment.of(variableList[i], Object.of(arg))
            operator.apply(substitution)
            log { "substitution $substitution" }
        }
        log { "getOperator: operator: $operator" }
        return operator
    }

    private fun getPlan(plan: String): Plan {
        val init = plan.indexOf("[")
        val last = plan.indexOf("]")
        println("getPlan: indexes $init $last \t string plan: $plan")
        val arguments = plan.drop(init).dropLast(last)
        println("getPlan: arguments $arguments")
        val index = 0
        val list = mutableListOf<Operator>()
        for (letter in arguments) {
            if (letter == ',') {
                list.add(getOperator(getAction(arguments.drop(index).dropLast(arguments.indexOf(letter))), parameters))
                log { "getPlan: indexes $init $last" }
            }
        }
        log { "getPlan: list $list" }
        return Plan.of(list)
    }

    private fun getState(state: String): State {
        // retrieve predicate by name
        val newState = state.substring(1, state.length - 1)
        println("getState: newState: $newState")
        var index = 0
        val list = mutableSetOf<Fluent>()
        val listParams = mutableListOf<Object>()
        var string = ""

        for (tmp in newState) {
            if (tmp == '(') {
                println("getState: indexOf('$tmp'): ${newState.indexOf(tmp)}")
                val predicateName = newState.substring(index, newState.indexOf(tmp))
                println("getState: predicateName from substring: $predicateName")
                val predicate = problem.domain.predicates.first { it.name == predicateName }
                println("getState: predicateName matched: $predicate")
                for (params in newState.substring(predicateName.length, newState.length)) {
                    if (params == ',' || params == ')') {
                        listParams.add(Object.of(string))
                        println("getState: param list: $listParams")
                        string = ""
                        index = newState.indexOf(params)
                        println("getState: index value: $index")
                    } else {
                        if (params != '(') {
                            println("getState: char to add: $params \t previous param: $string")
                            string = string.plus(params)
                            println("getState: new param: $string")
                        }
                    }
                }
                val fluent = Fluent.of(predicate, false, listParams)
                println("getState: fluent: $fluent")
                list.add(fluent)
            }
        }
        return State.of(list)
    }
}
