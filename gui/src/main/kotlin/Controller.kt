import core.* // ktlint-disable no-wildcard-imports
import domain.BlockWorldDomain
import explanation.Explainer
import explanation.ExplanationPresenter
import explanation.Question
import explanation.impl.* // ktlint-disable no-wildcard-imports
import javafx.scene.control.ComboBox
import utils.Debugger.log
import domain.BlockWorldDomain as BlockWorld
import domain.LogisticsDomain as Logistics

/**
 * Class representing the View of the application.
 */
class Controller {
    private lateinit var problem: Problem
    private lateinit var parameters: List<String>
    private lateinit var action: Action
    private lateinit var plan: Plan
    private lateinit var alternativePlan: Plan
    private lateinit var operator: Operator
    private lateinit var question: Question
    private lateinit var view: View
    var variableList = BlockWorldDomain.variables

    /**
     * triggers the computation of the [Question] chosen by the user.
     */
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
            plan = getPlan(formerPlanTextField.toString(), problem)
            if (!actionName.isNullOrBlank()) {
                action = getAction(actionName.toString(), problem)!!
                parameters = actionParametersComboBox.map { it.value }.filter { !it.isNullOrBlank() }
                log { "action parameters: $parameters" }
                operator = getOperator(action, parameters)
            }
        }
        if (!alternativePlanTextField.isNullOrBlank()) {
            alternativePlan = getPlan(alternativePlanTextField.toString(), problem)
        }
        initializeQuestion(questionType, actionPosition, stateTextField, alternativePlanTextField)
        val explanation = getExplanation(explanationType)
        view.showResult(explanation)
    }

    private fun initializeQuestion(
        questionType: CharSequence?,
        actionPosition: Int,
        stateTextField: CharSequence,
        alternativePlanTextField: CharSequence?,
    ) {
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
    }

    private fun getExplanation(explanationType: CharSequence?): String {
        val explanation = Explainer.of(Planner.strips()).explain(question)
        val explanationPresenter = ExplanationPresenter.of(explanation)
        var explanationString = if (explanationType?.startsWith("General")!!) {
            log { explanationType.toString() }
            explanationPresenter.present()
        } else {
            log { explanationType.toString() }
            explanationPresenter.presentMinimalExplanation()
        }
        return explanationString
    }

    /**
     * Method responsible for retrieving the list of the problems corresponding to input [Domain].
     */
    fun getProblems(domain: String): List<Problem> {
        return if (domain.startsWith("l").or(domain.startsWith("L"))) {
            Logistics.problems.toList()
        } else {
            BlockWorld.problems.toList()
        }
    }

    internal fun getProblem(domainName: String, problemName: String) =
        getProblems(domainName).first { it.name == problemName }

    internal fun getAction(actionName: String, problem: Problem): Action? {
        log { "getAction: string action: $actionName" }
        val actionMatched = try {
            problem.domain.actions.first { act -> act.name == actionName }
        } catch (e: NoSuchElementException) {
            null
        }
        log { "getAction: operator name from input string: $actionMatched" }
        return actionMatched
    }

    internal fun getObjectType(parameterName: String, problem: Problem): Any =
        problem.objects.map.forEach { (type, objectSet) ->
            if (
                objectSet.any { it.representation == parameterName }
            ) {
                log { "getParameterType: type: $type" }
                return type
            }
        }

    internal fun getOperator(action: Action, parameters: List<String>): Operator {
        var operator = Operator.of(action)
        log { "getOperator: action $action operator $operator parameters $parameters" }
        for ((i, arg) in parameters.withIndex()) {
            val obj = Object.of(arg)
            log { "arg: $arg variableList: ${variableList[i]} object ${obj.representation} " }
            val substitution = VariableAssignment.of(operator.parameters.keys.toList()[i], obj)
            log { "substitution $substitution" }
            operator = operator.apply(substitution)
            log { "operator after substitution: $operator" }
        }
        log { "getOperator: operator: $operator" }
        return operator
    }

    private fun getPlan(plan: String, problem: Problem): Plan {
        log { "getPlan: plan $plan" }
        val index = 0
        val list = mutableListOf<Operator>()
        val argsList = mutableListOf<String>()
        var string = ""
        lateinit var action: Action
        for (letter in plan) {
            if (letter == '(') {
                action = getAction(plan.substring(index, plan.indexOf(letter)), problem)!!
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
