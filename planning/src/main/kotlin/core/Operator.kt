package core

import core.impl.OperatorImpl

/**
 * An [Operator] represents an [Action] at runtime.
 */
interface Operator : Applicable<Operator>, Action {
    /**
     * @property args: list of the [Operator]'s value.
     */
    val args: List<Value>

    companion object {
        /**
         * Factory method for an [Operator] creation.
         */
        fun of(
            action: Action,
        ): Operator = OperatorImpl(
            action.name,
            action.parameters,
            action.preconditions,
            action.effects,
        )
    }
}
