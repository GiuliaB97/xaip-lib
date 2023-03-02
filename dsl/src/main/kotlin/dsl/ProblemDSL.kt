package dsl

import core.Domain
import core.FluentBasedGoal
import core.Goal
import core.ObjectSet
import core.Problem
import core.State
import dsl.provider.PredicateProvider
import dsl.provider.TypeProvider

/**
 * Class representing a [Problem] in the DSL.
 * @property domain: [Domain] created.
*/
class ProblemDSL(val domain: Domain) {
    /**
     * @property name: represents the name of the problem.
     */
    var name: String = ""
    /**
     * @property objects: represents the objects created.
     */
    var objects: ObjectSet = ObjectSet.of(emptyMap())

    /**
     * @property states: represents the inital [State] .
     */
    var state: State = State.of(emptySet())

    /**
     * @property goal: represents the [Goal].
     */
    var goal: Goal = FluentBasedGoal.of()

    private var predicateProvider = PredicateProvider.of(domain.predicates)
    private var typesProvider = TypeProvider.of(domain.types)

    /**
     * Method that allows to call [ObjectSetDSL] methods in an instance of a [ProblemDSL] without any qualifiers.
     */
    fun objects(f: ObjectSetDSL.() -> Unit) {
        val objectsDSL = ObjectSetDSL(typesProvider)
        objectsDSL.f()
        this.objects = objectsDSL.objectSet
    }

    /**
     * Method that allows to call [GoalDSL] methods in an instance of a [ProblemDSL] without any qualifiers.
     */
    fun goals(f: GoalDSL.() -> Unit) {
        val goalDSL = GoalDSL(predicateProvider)
        goalDSL.f()
        this.goal = goalDSL.toGoal()
    }

    /**
     * Method that allows to call [State] methods in an instance of a [ProblemDSL] without any qualifiers.
     */
    fun initialState(f: StatesDSL.() -> Unit) {
        val statesDSL = StatesDSL(predicateProvider)
        statesDSL.f()
        this.state = statesDSL.toState()
    }

    /**
     *  Method responsible that build an instance of [ProblemDSL] and converts it to a [Problem].
     */
    fun buildProblem(): Problem =
        Problem.of(domain, name, objects, state, goal)
}

/**
 * Entry point for [ProblemDSL] creation.
 */
fun problem(domain: Domain, f: ProblemDSL.() -> Unit): Problem {
    return ProblemDSL(domain).also(f).buildProblem()
}
