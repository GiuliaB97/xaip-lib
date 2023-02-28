package core

import core.impl.DomainImpl

/**
 * The [Domain] definition establishes the context of the world.
 * It determines what sorts of details the states can include.
 */
interface Domain {
    /**
     *
     */
    val name: String

    /**
     * @property predicates: facts allowed in the problem.
     */
    val predicates: Set<Predicate>

    /**
     * @property actions: set of actions allowed to move among states.
     */
    val actions: Set<Action>

    /**
     * @property types: types user can use for the objects' definition.
     */
    val types: Set<Type>

    /**
     * @property axioms: set of rules to be applied to the problem.
     */
    val axioms: Axiom?

    companion object {
        /**
         * Factory method for an [Domain] creation.
         */
        fun of(
            name: String,
            predicates: Set<Predicate>,
            actions: Set<Action>,
            types: Set<Type>,
            axioms: Axiom? = null,
        ): Domain = DomainImpl(name, predicates, actions, types, axioms)
    }
}
