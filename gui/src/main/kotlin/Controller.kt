/***
 * .
 */
import core.Problem
import domain.BlockWorldDomain as BlockWorld
import domain.LogisticsDomain as Logistics

class Controller {
    fun checkQuestion(
        comboBoxDomain: CharSequence?,
        comboBoxProblem: CharSequence?,
        comboBoxQuestion: CharSequence?,
        formerPlanTextField: CharSequence?,
        actionToRemoveTextField: CharSequence?,
        actionToAddTextField: CharSequence?,
        positionTextField: CharSequence,
        newPlanTextField: CharSequence,
    ) {
        println("arrivato $comboBoxDomain")
        when (comboBoxQuestion) {
            "Question 1" -> if (
                formerPlanTextField!!.isEmpty() ||
                actionToRemoveTextField!!.isEmpty() ||
                formerPlanTextField.isEmpty() ||
                positionTextField.isEmpty()
            ) {
                error("missing fields")
            }
            else -> println("")
        }
    }

    fun getDomain(domain: String): List<Problem> {
        return if (domain.startsWith("b").or(domain.startsWith("B"))) {
            BlockWorld.problems.toList()
        } else {
            Logistics.problems.toList()
        }
    }
}
