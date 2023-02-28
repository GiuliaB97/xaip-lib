package core

import core.impl.ProblemImpl
/**
 * A [Problem] express the global worldly aspects of a problem planned.
 *
 * @property domain: [Domain] of the [Problem].
 * @property objects: entities present in the [Problem].
 * @property initialState: starting state of the [Problem].
 * @property goal: objective of the [Problem].
 */
interface Problem {
    /**
     * @property domain: [Domain] of the [Problem].
     */
    val domain: Domain

    /**
     * @property objects: entities present in the [Problem].
     */
    val objects: ObjectSet

    /**
     * @property initialState: starting state of the [Problem].
     */
    val initialState: State

    /**
     * @property goal: objective of the [Problem].
     */
    val goal: Goal

    companion object {
        /**
         * Factory method for an [Problem] creation.
         */
        fun of(
            domain: Domain,
            objects: ObjectSet,
            initialState: State,
            goal: Goal,
        ): Problem = ProblemImpl(domain, objects, initialState, goal)
    }
}
