package dsl

import core.Fluent
import core.FluentBasedGoal
import dsl.provider.PredicateProvider

/**
 * Class representing a [FluentBasedGoal] in the DSL.
 */
class GoalDSL(
    predicateProvider: PredicateProvider,
) : AbstractFluentDSL(predicateProvider) {
    /**
     * @property fluents: set of [Fluent] that compose the goal.
     */
    var fluents: MutableSet<Fluent> = mutableSetOf()

    /**
     * Method that updates the internal list of [fluents] adding the last fluent created.
     */
    operator fun Fluent.unaryPlus() = fluents.add(this)

    /**
     * Method responsible for the creation of the [FluentBasedGoal].
     */
    fun toGoal(): FluentBasedGoal = FluentBasedGoal.of(fluents)
}
