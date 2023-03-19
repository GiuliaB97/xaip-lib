import Components.actionNameComboBox
import Components.actionParameter1ComboBox
import Components.actionParameter2ComboBox
import Components.actionParameter3ComboBox
import Components.actionPositionTextField
import Components.domainComboBox
import Components.explanationTypeComboBox
import Components.formerPlanTextField
import Components.newPlanTextField
import Components.problemNameComboBox
import Components.questionComboBox
import Components.stateTextField
import Components.submit
import GuiGrid.initGrid
import Label.actionLabel
import Label.actionParameterLabel
import Label.domainLabel
import Label.explanationTypeLabel
import Label.formerPlanLabel
import Label.positionLabel
import Label.problemLabel
import Label.questionLabel
import Label.stateLabel
import Value.domain
import Value.problemList
import Value.values
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.* // ktlint-disable no-wildcard-imports
import javafx.stage.Stage

class View(private val primaryStage: Stage, private val controller: Controller) {
    private val controlList: List<Control> = listOf(
        // domainLabel, domainComboBox,
        problemLabel, questionLabel, actionLabel, positionLabel, formerPlanLabel,
        actionParameterLabel, stateLabel,
        // explanationTypeLabel, explanationTypeComboBox,
        problemNameComboBox, questionComboBox, actionNameComboBox, actionParameter3ComboBox,
        actionParameter2ComboBox, actionParameter1ComboBox,
        formerPlanTextField, newPlanTextField,
        actionPositionTextField, stateTextField,
        // submit
    )
    private val parameterList: List<Control> = listOf(
        actionParameter1ComboBox,
        actionParameter2ComboBox,
        actionParameter3ComboBox,
    )

    private fun reset(list: List<Control>) {
        explanationTypeLabel.isVisible = true
        explanationTypeComboBox.isVisible = true
        for (elem in list) {
            elem.isVisible = false
        }
    }

    private fun listeners() {
        explanationTypeComboBox.setCellFactory {
            val cell: ListCell<String?> = object : ListCell<String?>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = if (empty) null else item
                }
            }
            cell.setOnMousePressed {
                if (!cell.isEmpty) {
                    println("Click on " + cell.item)
                    domainLabel.isVisible = true
                    domainComboBox.isVisible = true
                }
            }
            cell
        }

        domainComboBox.setCellFactory {
            val cell: ListCell<String?> = object : ListCell<String?>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = if (empty) null else item
                }
            }
            cell.setOnMousePressed {
                if (!cell.isEmpty) {
                    println("Click on " + cell.item)
                    reset(controlList)
                    problemList = cell.item?.let { it1 -> controller.getProblems(it1) }!!
                    domain = problemList.first().domain
                    problemNameComboBox.items = FXCollections.observableArrayList<String?>()
                        .also { it.addAll(problemList.map { it.name }) }
                    actionNameComboBox.items = FXCollections.observableArrayList<String?>()
                        .also { it.addAll(domain.actions.map { it.name }) }
                    values = problemList.first().objects.map.values.map { it.map { it.representation } }.flatten()
                    actionParameter1ComboBox.items = FXCollections.observableArrayList<String?>()
                        .also { it.addAll(values) }
                    actionParameter2ComboBox.items = FXCollections.observableArrayList<String?>()
                        .also { it.addAll(values) }
                    actionParameter3ComboBox.items = FXCollections.observableArrayList<String?>()
                        .also { it.addAll(values) }
                    problemNameComboBox.isVisible = true
                    problemLabel.isVisible = true
                }
            }
            cell
        }

        problemNameComboBox.setCellFactory {
            val cell: ListCell<String?> = object : ListCell<String?>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = if (empty) null else item
                }
            }
            cell.setOnMousePressed {
                if (!cell.isEmpty) {
                    println("Click on " + cell.item)
                    questionComboBox.isVisible = true
                    questionLabel.isVisible = true
                }
            }
            cell
        }

        questionComboBox.setCellFactory {
            val cell: ListCell<String?> = object : ListCell<String?>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = if (empty) null else item
                }
            }
            cell.setOnMousePressed {
                if (!cell.isEmpty) {
                    println("Click on " + cell.item)
                    cell.item?.let {
                        initAction(it)
                    }!!
                    formerPlanLabel.isVisible = true
                    formerPlanTextField.isVisible = true
                }
            }
            cell
        }

        actionNameComboBox.setCellFactory {
            val cell: ListCell<String?> = object : ListCell<String?>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = if (empty) null else item
                }
            }
            cell.setOnMousePressed {
                if (!cell.isEmpty) {
                    println("Click on " + cell.item)
                    cell.item?.let {
                        val action = domain.actions.first { action -> action.name == it }

                        val size = action.parameters.size
                        actionParameterLabel.isVisible = false
                        actionParameter1ComboBox.isVisible = false
                        actionParameter2ComboBox.isVisible = false
                        actionParameter3ComboBox.isVisible = false

                        if (size > 0) {
                            actionParameterLabel.isVisible = true
                            actionParameter1ComboBox.isVisible = true
                        }
                        if (size > 1) {
                            actionParameter2ComboBox.isVisible = true
                        }
                        if (size > 2) {
                            actionParameter3ComboBox.isVisible = true
                        }
                        submit.isDisable = false
                    }!!
                }
            }
            cell
        }

        submit.onAction = EventHandler {
            if (questionComboBox.value != null &&
                problemNameComboBox.value != null &&
                domainComboBox.value != null
            ) {
                println("submit question: ${questionComboBox.value}")
                controller.checkQuestion(
                    domainComboBox.value,
                    problemNameComboBox.value,
                    questionComboBox.value,
                    formerPlanTextField.characters,
                    actionNameComboBox.value,
                    actionPositionTextField.value,
                    newPlanTextField.characters,
                    explanationTypeComboBox.value,
                    parameterList as List<ComboBox<String>>,
                    stateTextField.characters,
                )
            }
        }
    }

    /***
     * .
     */
    private fun createGui() {
        val grid = initGrid(primaryStage)
        val scene = Scene(grid, 700.0, 550.0)
        listeners()
        primaryStage.scene = scene
        primaryStage.show()
    }

    fun show() {
        reset(controlList)
        createGui()
    }

    private fun initAction(questionType: String) {
        when (questionType) {
            "Question 1" -> {
                actionLabel.text = "Action to remove"
                actionLabel.isVisible = true
                actionNameComboBox.isVisible = true
                newPlanTextField.isVisible = false
            }
            "Question 2" -> {
                actionLabel.text = "Action to add"
                actionLabel.isVisible = true
                actionNameComboBox.isVisible = true
                positionLabel.isVisible = true
                actionPositionTextField.isVisible = true
                newPlanTextField.isVisible = false
            } "Question 3" -> {
                actionLabel.text = "Action to replace"
                actionLabel.isVisible = true
                actionNameComboBox.isVisible = true
                positionLabel.isVisible = true
                actionPositionTextField.isVisible = true
                newPlanTextField.isVisible = false
                stateLabel.isVisible = true
                stateTextField.isVisible = true
            } "Question 4" -> {
                actionLabel.text = "New plan"
                actionLabel.isVisible = true
                actionNameComboBox.isVisible = false
                newPlanTextField.isVisible = true
                actionPositionTextField.isVisible = false
                positionLabel.isVisible = false
            }
        }
    }
}
