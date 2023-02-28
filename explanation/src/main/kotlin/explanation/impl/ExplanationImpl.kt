package explanation.impl

import core.* // ktlint-disable no-wildcard-imports
import core.utility.finalStateComplaintWithGoal
import core.utility.then
import explanation.Explainer
import explanation.Explanation
import explanation.Question
import explanation.Simulator
import explanation.utils.replaceArtificialOperator
import explanation.utils.retrieveArtificialOperator
/**
 * Reification of the [Explanation] interface.
 */
internal data class ExplanationImpl(
    override val question: Question,
    override val explainer: Explainer,
) : Explanation {
    override var novelPlan: Plan = Plan.of(emptyList())

    override val originalPlan: Plan by lazy {
        this.question.plan
    }
    override val addedList: List<Operator> by lazy {
        this.novelPlan.operators.filter {
            !this.originalPlan.operators.contains(it)
        }
    }
    override val deletedList: List<Operator> by lazy {
        this.originalPlan.operators.filter {
            !this.novelPlan.operators.contains(it)
        }
    }
    override val sharedList: List<Operator> by lazy {
        this.originalPlan.operators.filter {
            this.novelPlan.operators.contains(it)
        }
    }
    private val simulator = Simulator.of()
    private val minimalPlan by lazy {
        explainer.minimalPlanSelector(question.problem)
    }

    init {
        when (question) {
            is QuestionReplaceOperator -> {
                val operatorsToKeep = question.plan.operators.subList(0, question.focusOn).toMutableList()
                novelPlan = Plan.of(
                    operatorsToKeep
                        .also { it.add(question.focus) }
                        .also {
                            it.addAll(
                                try {
                                    explainer.planner.plan(
                                        question.buildHypotheticalProblem().first(),
                                    ).first().operators
                                } catch (_: Exception) {
                                    throw IllegalArgumentException(
                                        "Error: the operator ${question.focus} is not applicable to the state chosen:" +
                                            " ${(question.inState == null)
                                                .then(question.problem.initialState) ?: question.inState}",
                                    )
                                },
                            )
                        },
                )
            }
            is QuestionAddOperator -> {
                val planCopy = question.plan.operators.toMutableList()
                planCopy.add(question.focusOn, question.focus)
                novelPlan = (
                    (planCopy.retrieveArtificialOperator() != null) then
                        Plan.of(planCopy.replaceArtificialOperator(question.problem.domain.actions))
                    ) ?: Plan.of(planCopy)
            }
            is QuestionRemoveOperator -> {
                novelPlan = explainer.planner.plan(question.buildHypotheticalProblem().first()).first()
                if (novelPlan.operators.retrieveArtificialOperator() != null) {
                    novelPlan =
                        Plan.of(novelPlan.operators.replaceArtificialOperator(question.problem.domain.actions))
                }
            }
            is QuestionPlanProposal -> novelPlan = question.alternativePlan
            is QuestionPlanSatisfiability -> novelPlan = question.plan
        }
    }

    override fun isPlanValid(): Boolean {
        val states = simulator.simulate(novelPlan, question.problem.initialState)
        return if (states.isNotEmpty()) {
            states.all { finalStateComplaintWithGoal(question.problem.goal as FluentBasedGoal, it) }
        } else {
            false
        }
    }

    /**
     * */
    override fun isPlanLengthAcceptable(): Boolean =
        minimalPlan.operators.size <= novelPlan.operators.size

    /**
     * */
    override fun isProblemSolvable(): Boolean =
        minimalPlan.operators.isNotEmpty()

    override fun minimalSolutionLength(): Int = minimalPlan.operators.size

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExplanationImpl

        if (this.originalPlan != other.originalPlan) return false
        if (this.novelPlan != other.novelPlan) return false

        return true
    }
}
