package core.impl

import core.Domain
import core.Goal
import core.ObjectSet
import core.Problem
import core.State

internal data class ProblemImpl(
    override val domain: Domain,
    override val name: String,
    override val objects: ObjectSet,
    override val initialState: State,
    override val goal: Goal
) : Problem {
    override fun toString(): String =
        """${Problem::class.simpleName}(
            |  ${Problem::domain.name}=${domain.toString().replace("\n", "\n  ")},
            |  ${Problem::name}=$name,
            |  ${Problem::objects.name}=$objects,
            |  ${Problem::initialState.name}=$initialState,
            |  ${Problem::goal.name}=$goal,
            |)
        """.trimMargin()
}
