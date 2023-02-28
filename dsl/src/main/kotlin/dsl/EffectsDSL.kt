package dsl

import core.Effect
import core.Fluent
import dsl.provider.PredicateProvider

/**
 * Class representing multiple [Effect]s in the DSL.
*/
class EffectsDSL(
    predicateProvider: PredicateProvider
) : AbstractFluentDSL(predicateProvider) {
    /**
     * @property effects: set of [Effect] created.
     */
    var effects: MutableSet<Effect> = mutableSetOf()

    /**
     * Method that updates the internal list of [effects] adding the last positive fluent created.
     */
    operator fun Fluent.unaryPlus() = effects.add(core.Effect.positive(this))

    /**
     * Method that create a positive [Fluent] from a [String] without arguments.
     */
    operator fun String.unaryPlus() = effects.add(Effect.positive(this()))

    /**
     * Method that updates the internal list of [effects] adding the last negative fluent created.
     */
    operator fun Fluent.unaryMinus() = effects.add(core.Effect.negative(this))

    /**
     * Method that create a negative [Type] from a [String] without arguments.
     */
    operator fun String.unaryMinus() = effects.add(Effect.negative(this()))
}
