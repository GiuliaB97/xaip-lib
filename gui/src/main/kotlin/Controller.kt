import core.* // ktlint-disable no-wildcard-imports
import domain.BlockWorldDomain.variables
import explanation.Explainer
import explanation.ExplanationPresenter
import explanation.Question
import explanation.impl.*
import javafx.scene.control.ComboBox
import utils.BaseClass
import domain.BlockWorldDomain as BlockWorld
import domain.LogisticsDomain as Logistics

/**
 * Class representing the View of the application.
 */
class Controller : BaseClass() {

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
        log { "checkQuestion: $domainName \t question: $questionType" }
        log { "checkQuestion: $domainName" }
        if (
            formerPlanTextField!!.isEmpty()
        ) {
            error("checkQuestion: Question 1: missing fields")
        } else {
            problem = getProblem(domainName.toString(), problemName.toString())
            plan = getPlan(formerPlanTextField.toString())
            if (!actionName.isNullOrBlank()) {
                action = getAction(actionName.toString())!!
                parameters = actionParametersComboBox.map { it.value }.filter { !it.isNullOrBlank() }
                log { "action parameters: $parameters" }
                operator = getOperator(action, parameters)
            }
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
                log { "checkQuestion: Question 3: $question" }
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
        var explanationString = if (explanationType?.startsWith("General")!!) {
            log { explanationType.toString() }
            explanationPresenter.present()
        } else {
            log { explanationType.toString() }
            explanationPresenter.presentMinimalExplanation()
        }
        view.showResult(explanationString)
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

    private fun getAction(operatorName: String): Action? {
        log { "getAction: string operator: $operatorName" }
        val actionMatched = try { problem.domain.actions.first { act -> act.name == operatorName } } catch (e: NoSuchElementException) { null }
        log { "getAction: operator name from input string: $actionMatched" }
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
        var operator = Operator.of(action)
        val variableList = variables // listOf(Variable.of("X"), Variable.of("Y"), Variable.of("Z"))
        log { "getOperator: operator $operator parameters $parameters" }
        for ((i, arg) in parameters.withIndex()) {
            log { "arg: $arg variableList: ${variableList[i] } object ${Object.of(arg)}" }
            val substitution = VariableAssignment.of(variableList[i], Object.of(arg))
            log { "substitution $substitution" }
            operator = operator.apply(substitution)
            log { "operator after substitution: $operator" }
        }
        log { "getOperator: operator: $operator" }
        return operator
    }

    private fun getPlan(plan: String): Plan {
        log { "getPlan: plan $plan" }
        val index = 0
        val list = mutableListOf<Operator>()
        val argsList = mutableListOf<String>()
        var string = ""
        lateinit var action: Action
        for (letter in plan) {
            if (letter == '(') {
                action = getAction(plan.substring(index, plan.indexOf(letter)))!!
                log { "getPlan: action ${action.name}" }
                log { "getPlan: plan $plan start index ${plan.indexOf(letter)}, end index ${plan.indexOf(')')}" }
                val args = plan.substring(plan.indexOf(letter) + 1, plan.length)
                log { "getPlan: args from substring $args" }
                for (argument in args) {
                    log { "getPlan: char in args: $argument" }
                    if (argument == ',') {
                        argsList.add(string)
                        log { "getPlan: arg added: $string" }
                    } else if (argument == ')') {
                        argsList.add(string)
                        log { "getPlan: end of the operator" }
                        break
                    } else {
                        log { "getPlan: arg string: $argument until char: $argument" }
                        string = string.plus(argument)
                    }
                    log { "getPlan: next action" }
                }
            } else if (letter == ')') {
                list.add(getOperator(action, argsList))
            }
        }
        log { "getPlan: list $list" }
        return Plan.of(list)
    }

    private fun getState(state: String): State {
        // retrieve predicate by name
        val newState = state.substring(1, state.length - 1)
        log { "getState: newState: $newState" }
        var index = 0
        val list = mutableSetOf<Fluent>()
        val listParams = mutableListOf<Object>()
        var string = ""

        for (tmp in newState) {
            if (tmp == '(') {
                log { "getState: indexOf('$tmp'): ${newState.indexOf(tmp)}" }
                val predicateName = newState.substring(index, newState.indexOf(tmp))
                log { "getState: predicateName from substring: $predicateName" }
                val predicate = problem.domain.predicates.first { it.name == predicateName }
                log { "getState: predicateName matched: $predicate" }
                for (params in newState.substring(predicateName.length, newState.length)) {
                    if (params == ',' || params == ')') {
                        listParams.add(Object.of(string))
                        log { "getState: param list: $listParams" }
                        string = ""
                        index = newState.indexOf(params)
                        log { "getState: index value: $index" }
                    } else {
                        if (params != '(') {
                            log { "getState: char to add: $params \t previous param: $string" }
                            string = string.plus(params)
                            log { "getState: new param: $string" }
                        }
                    }
                }
                val fluent = Fluent.of(predicate, false, listParams)
                log { "getState: fluent: $fluent" }
                list.add(fluent)
            }
        }
        return State.of(list)
    }
}
