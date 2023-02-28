package explanation

import core.Operator
import core.Plan
import explanation.impl.ExplanationImpl

/**
 *An [Explanation] is an entity that give a contrastive explanation about a [Plan].
 */
interface Explanation {
    /**
     * @property explainer: represents the explainer provided by the user.
     */
    val explainer: Explainer

    /**
     * @property originalPlan: represents the original [Plan] the inquirer wants to challenge.
     */
    val originalPlan: Plan

    /**
     * @property novelPlan: represents the new [Plan] containing the inquirer's suggestions.
     */
    val novelPlan: Plan

    /**
     * @property question: represents the question asked by the inquirer.
     */
    val question: Question

    /**
     * @property addedList: represents the list of the actions that where not present in the [originalPlan]
     * but are in the [novelPlan].
     */
    val addedList: List<Operator>

    /**
     * @property deletedList: represents the list of the actions that where present in the [originalPlan]
     * but are not in the [novelPlan]
     */
    val deletedList: List<Operator>

    /**
     * @property sharedList: represents the list of the actions that are present both in the [novelPlan]
     * and in the [originalPlan].
     */
    val sharedList: List<Operator>

    /**
     * Method that states if a [Plan] is a solution for a given [Problem].
     */
    fun isPlanValid(): Boolean

    /**
     * Method that states if a [Problem] is solvable.
     */
    fun isProblemSolvable(): Boolean

    /**
     * Method that states if a [Problem] is solvable.
     */
    fun minimalSolutionLength(): Int

    /**
     * Method that states if the legth of the [Plan] proposed is acceptabl.
     */
    fun isPlanLengthAcceptable(): Boolean

    companion object {
        /**
         * Factory method for an [Explanation] creation.
         */
        fun of(
            question: Question,
            explainer: Explainer,
        ): Explanation = ExplanationImpl(question, explainer)
    }
}
