package core

import core.impl.ActionImpl

/**
 * An [Action] represents a way for changing the state of the world.
 * @property effects: describe the effects of the action.
 * */
interface Action {
    /**
     * @property name: states the name of the action.
     */
    val name: String

    /**
     * @property parameters: is a map containing the variables and their types.
     */
    val parameters: Map<Variable, Type>

    /**
     * @property preconditions: is a goal description that must be satisfied before the action is applied.
     */
    val preconditions: Set<Fluent>

    /**
     * @property effects: describes the effects of the action.
     */
    val effects: Set<Effect>

    /**
     * @property positiveEffects: describes the positive effects of the action.
     */
    val positiveEffects: Set<Effect>

    /**
     * @property negativeEffects: describes the negative effects of the action.
     */
    val negativeEffects: Set<Effect>

    companion object {
        /**
         * Factory method for an [Action] creation.
         */
        fun of(
            name: String,
            parameters: Map<Variable, Type>,
            preconditions: Set<Fluent>,
            effects: Set<Effect>,
        ): Action = ActionImpl(name, parameters, preconditions, effects)
    }
}
