import Components.actionComboBox
import Components.actionParameter1ComboBox
import Components.actionParameter2ComboBox
import Components.actionParameter3ComboBox
import Components.actionPositionTextField
import Components.domainComboBox
import Components.formerPlanTextField
import Components.newPlanTextField
import Components.problemComboBox
import Components.questionComboBox
import Components.submit
import GuiGrid.initGrid
import Label.actionLabel
import Label.actionParameterLabel
import Label.formerPlanLabel
import Label.positionLabel
import Label.problemLabel
import Label.questionLabel
import Value.domain
import Value.problemList
import Value.values
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.* // ktlint-disable no-wildcard-imports
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import java.awt.Component

class View(private val primaryStage: Stage, private val controller: Controller) {
    private val controlList = listOf(
        // domainLabel, domainComboBox,
        problemLabel, questionLabel, actionLabel, positionLabel, formerPlanLabel, actionParameterLabel,
        problemComboBox, questionComboBox, actionComboBox, actionParameter3ComboBox,
        actionParameter2ComboBox, actionParameter1ComboBox,
        formerPlanTextField, newPlanTextField, actionPositionTextField,
        // submit
    )

    private fun reset(list: List<Control>) {
        for (elem in list) {
            elem.isVisible = false
        }
    }

    private fun listeners() {
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
                    problemList = cell.item?.let { it1 -> controller.getDomain(it1) }!!
                    domain = problemList.first().domain
                    val emptyObservableList: ObservableList<String> = FXCollections.observableArrayList<String?>()
                        .also { it.addAll(problemList.map { it.name }) }
                    println("list:$emptyObservableList")
                    problemComboBox.items = emptyObservableList
                    actionComboBox.items = FXCollections.observableArrayList<String?>()
                        .also { it.addAll(domain.actions.map { it.name }) }
                    values = problemList.first().objects.map.values.map { it.map { it.representation } }.flatten()
                    actionParameter1ComboBox.items = FXCollections.observableArrayList<String?>()
                        .also { it.addAll(values) }
                    actionParameter2ComboBox.items = FXCollections.observableArrayList<String?>()
                        .also { it.addAll(values) }
                    actionParameter3ComboBox.items = FXCollections.observableArrayList<String?>()
                        .also { it.addAll(values) }
                    problemComboBox.isVisible = true
                    problemLabel.isVisible = true
                }
            }
            cell
        }

        problemComboBox.setCellFactory {
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
                }
            }
            cell
        }

        actionComboBox.setCellFactory {
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
                problemComboBox.value != null &&
                domainComboBox.value != null
            ) {
                controller.checkQuestion(
                    domainComboBox.value,
                    problemComboBox.value,
                    questionComboBox.value,
                    formerPlanTextField.characters,
                    actionComboBox.value,
                    newPlanTextField.characters,
                )
            }
        }
    }

    /***
     * .
     */
    private fun createGui() {
        val grid = initGrid(primaryStage)
        val scene = Scene(grid, 700.0, 350.0)
        listeners()
        primaryStage.scene = scene
        primaryStage.show()
    }

    fun show() {
        reset(controlList)
        createGui()

    }

    private fun initAction(questionType: String) {
        if (questionType == "Question 1") {
            actionLabel.text = "Action to remove"
            actionLabel.isVisible = true
            actionComboBox.isVisible = true
            newPlanTextField.isVisible = false
        } else if (questionType == "Question 2") {
            actionLabel.text = "Action to add"
            actionLabel.isVisible = true
            actionComboBox.isVisible = true
            positionLabel.isVisible = true
            actionPositionTextField.isVisible = true
            newPlanTextField.isVisible = false
        } else if (questionType == "Question 3") {
            actionLabel.text = "Action to replace"
            actionLabel.isVisible = true
            actionComboBox.isVisible = true
            positionLabel.isVisible = true
            actionPositionTextField.isVisible = true
            newPlanTextField.isVisible = false
        } else if (questionType == "Question 4") {
            actionLabel.text = "Plan comparison"
            actionLabel.isVisible = true
            actionComboBox.isVisible = false
            newPlanTextField.isVisible = true
            actionPositionTextField.isVisible = false
            positionLabel.isVisible = false
        }
    }
}