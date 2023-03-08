import core.Operator
import core.Plan
import core.Problem
import core.State
import explanation.Question
import explanation.impl.*

/***
 * .
 */

class Model(
    val problem: Problem,
    val questionType: Int,
    val action: Operator?,
    val plan: Plan,
    val alternativePlan: Plan?,
    val position: Int?,
    val inState: State?,
) {
    private lateinit var question: Question

    // Nota possibile problema di subtyping controlla come ha fatto GC, per applicable
    fun init() {
        when (questionType) {
            1 -> question = QuestionRemoveOperator(problem, plan, action!!)
            2 -> question = QuestionAddOperator(problem, plan, action!!, position!!)
            3 -> question = QuestionReplaceOperator(problem, plan, action!!, position!!)
            4 -> question = QuestionPlanProposal(problem, plan, alternativePlan!!)
            5 -> question = QuestionPlanSatisfiability(problem, plan)
        }
    }

    fun getQuestion() = question
}
