package core

import core.impl.AxiomImpl

/**
 * Axioms are logical formulas that assert relationships among propositions
 * that hold within a situation.
 * */
interface Axiom {
    /**
     * @property parameters: is the field that contains all the variables that appear in
     * the axiom along with their type.
     */
    val parameters: Map<Variable, Type>

    /**
     * @property context: rules stated by the [Axiom].
     */
    val context: Expression

    /**
     * @property implies: consequences always true because of the [Axiom].
     */
    val implies: Expression

    companion object {
        /**
         * Factory method for an [Axiom] creation.
         */
        fun of(
            parameters: Map<Variable, Type>,
            context: Expression,
            implies: Expression,
        ): Axiom = AxiomImpl(parameters, context, implies)
    }
}
