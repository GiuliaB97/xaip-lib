package explanation.impl

import core.* // ktlint-disable no-wildcard-imports
import explanation.Question

/**
 * Abstract class for [Question] providing some template methods to be used by the subclasses.
 */
abstract class BaseQuestion : Question {

    protected abstract val newPredicate: Predicate

    protected abstract val newGroundFluent: Fluent

    protected abstract val newFluent: Fluent

    protected abstract val oldAction: Action

    protected abstract val newAction: Action

    protected abstract val hDomain: Domain

    /**
     * returns a new fluent using [predicate] as [Predicate] and the [operator]' arguments as values.
     */
    @Suppress("SpreadOperator")
    fun createNewGroundFluent(operator: Operator, predicate: Predicate): Fluent =
        Fluent.positive(predicate, *operator.args.toTypedArray())

    /**
     * returns a new fluent using [predicate] as [Predicate] and the [operator]' parameters as values.
     */
    @Suppress("SpreadOperator")
    fun createNewFluent(operator: Operator, predicate: Predicate): Fluent =
        Fluent.positive(predicate, *operator.parameters.keys.toTypedArray())

    /**
     * returns e a new [Predicate] named [name], and the [action]'s parameters as value.
     */
    fun createNewPredicate(action: Action, name: String): Predicate =
        Predicate.of(name + action.name, action.parameters.values.toList())

    /**
     * returns a new [Action] with same signature of [] action except for;
     * the name; that is the name of [action] followed by "^" and the set of pre/post-conditions
     * that will include [fluent] according to the [negated] parameter.
     */
    fun createNewAction(action: Action, fluent: Fluent, negated: Boolean = false): Action {
        return Action.of(
            name = action.name + "^",
            parameters = action.parameters,
            preconditions = action.preconditions,
            effects = when (negated) {
                true -> mutableSetOf(Effect.negative(fluent)).also { it.addAll(action.effects) }
                else -> mutableSetOf(Effect.of(fluent)).also { it.addAll(action.effects) }
            },
        )
    }

    /**
     * returns a [Domain] with the constraints suggested by the user.
     */
    fun buildHdomain(domain: Domain, newPredicate: Predicate, newAction: Action) =
        Domain.of(
            name = domain.name,
            predicates = mutableSetOf(newPredicate).also { it.addAll(domain.predicates) },
            actions = mutableSetOf(newAction).also {
                domain.actions.map { oldAction ->
                    if (oldAction.name != newAction.name.filter { char -> char.isLetter() }) {
                        it.add(oldAction) // se il nome è diverso lo aggiungo
                    }
                }
            },
            types = domain.types,
        )

    /**
     * returns a [Problem] with the constraints suggested by the user.
     */
    fun buildHproblem(
        hDomain: Domain,
        problem: Problem,
        newFluent: Fluent?,
        state: State?,
        updateState: Boolean = false,
    ) =
        Problem.of(
            domain = hDomain,
            objects = problem.objects,
            initialState = when (updateState) {
                true -> State.of(mutableSetOf(newFluent!!).also { it.addAll(problem.initialState.fluents) })
                else -> state ?: problem.initialState
            },
            goal = when (newFluent) {
                null -> problem.goal
                else -> FluentBasedGoal.of(
                    (problem.goal as FluentBasedGoal).targets.toMutableSet()
                        .also { it.add(newFluent) },
                )
            },
        )
}
