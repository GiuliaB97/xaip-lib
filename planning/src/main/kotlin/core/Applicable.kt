package core

import core.impl.Context

/**
 * Generic type for an applicable entity.
 */
interface Applicable<Self : Applicable<Self>> {
    /**
     * Method responsible for the application of a logic substitution to the entity.
     */
    fun apply(substitution: VariableAssignment): Self

    /**
     * Method responsible for the refreshing of variables.
     * Mainly used to avoid spurious substitutions.
     */
    fun refresh(scope: Context = Context.empty()): Self
}
