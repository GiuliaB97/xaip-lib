import core.Problem
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.* // ktlint-disable no-wildcard-imports
import javafx.scene.layout.GridPane
import javafx.stage.Stage

class View(private val primaryStage: Stage, private val controller: Controller) {
    private val domainLabel = Label("Domain")
    private val problemLabel = Label("Problem")
    private val questionLabel = Label("Question type")
    private val actionToRemoveLabel = Label("Action to remove")
    private val actionToAddLabel = Label("Action to add")
    private val positionLabel = Label("Position new action")
    private val formerPlanLabel = Label("Former plan")
    private val newPlanLabel = Label("New plan")

    private var problemList = emptyList<Problem>()

    private var domainList: ObservableList<String> = FXCollections.observableArrayList(
        "Block world",
        "Logistics",
    )

    private var questionList: ObservableList<String> = FXCollections.observableArrayList(
        "Question 1",
        "Question 2",
        "Question 3",
        "Question 4",
        "Question 5",
    )

    private val comboBoxDomain = ComboBox(domainList)
    private var comboBoxProblem = ComboBox(FXCollections.observableArrayList("                  ")) // by lazy { problems.forEach { emptyObservableList.add(it.name) }; ComboBox(emptyObservableList) }
    private val comboBoxQuestion = ComboBox(questionList)
    private val formerPlanTextField = TextField()
    private val actionToRemoveTextField = TextField()
    private val actionToAddTextField = TextField()
    private val positionTextField = TextField()
    private val newPlanTextField = TextField()

    private var domainError = TextField()
    private var problemError = TextField()
    private var questionError = TextField()
    private var formerPlanError = TextField()
    private var actionToRemoveError = TextField()
    private var actionToAddError = TextField()
    private var positionTextError = TextField()
    private var newPlanError = TextField()

    private val submit = Button("Submit")

    /***
     * .
     */

    private val grid = GridPane()
    private fun createGrid() {
        comboBoxDomain.setCellFactory {
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
                    val emptyObservableList: ObservableList<String> = FXCollections.observableArrayList<String?>().also { it.addAll(problemList.map { it.name }) }
                    println("list:$emptyObservableList")
                    comboBoxProblem = ComboBox(emptyObservableList)
                    grid.add(comboBoxProblem, 1, 2)
                }
            }
            cell
        }

        problemError.isVisible = false
        questionError.isVisible = false
        formerPlanError.isVisible = false
        actionToRemoveError.isVisible = false
        actionToAddError.isVisible = false
        positionTextError.isVisible = false
        newPlanError.isVisible = false

        problemError.isEditable = false
        questionError.isEditable = false
        formerPlanError.isEditable = false
        actionToRemoveError.isEditable = false
        actionToAddError.isEditable = false
        positionTextError.isEditable = false
        newPlanError.isEditable = false

        grid.add(problemLabel, 0, 2)
        grid.add(comboBoxProblem, 1, 2)
        grid.add(problemError, 2, 2)

        grid.add(questionLabel, 0, 3)
        grid.add(comboBoxQuestion, 1, 3)
        grid.add(questionError, 2, 3)

        grid.add(formerPlanLabel, 0, 4)
        grid.add(formerPlanTextField, 1, 4)
        grid.add(formerPlanError, 2, 4)

        grid.add(actionToRemoveLabel, 0, 5)
        grid.add(actionToRemoveTextField, 1, 5)
        grid.add(actionToRemoveError, 2, 5)

        grid.add(actionToAddLabel, 0, 6)
        grid.add(actionToAddTextField, 1, 6)
        grid.add(actionToAddError, 2, 6)

        grid.add(positionLabel, 0, 7)
        grid.add(positionTextField, 1, 7)
        grid.add(positionTextError, 2, 7)

        grid.add(newPlanLabel, 0, 8)
        grid.add(newPlanTextField, 1, 8)
        grid.add(newPlanError, 2, 8)

        grid.add(submit, 1, 9)

        val scene = Scene(grid, 700.0, 350.0)
        primaryStage.scene = scene

        submit.onAction = EventHandler {
            if (comboBoxDomain.value == null) {
                domainError.isVisible = true
                domainError.text = "Error, select an item"
            }
            if (comboBoxProblem.value == null) {
                problemError.isVisible = true
                problemError.text = "Error, select an item"
            }
            if (comboBoxQuestion.value == null) {
                problemError.isVisible = true
                questionError.text = "Error, select an item"
            }
            if (comboBoxQuestion.value != null &&
                comboBoxProblem.value != null &&
                comboBoxDomain.value != null
            ) {
                controller.checkQuestion(
                    comboBoxDomain.value,
                    comboBoxProblem.value,
                    comboBoxQuestion.value,
                    formerPlanTextField.characters,
                    actionToRemoveTextField.characters,
                    actionToAddTextField.characters,
                    positionTextField.characters,
                    newPlanTextField.characters,
                )
            }
        }
    }

    private fun createDomain() {
        grid.alignment = Pos.CENTER
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(25.0, 25.0, 25.0, 25.0)

        primaryStage.title = "xaip-lib-app"

        grid.add(domainLabel, 0, 1)
        grid.add(comboBoxDomain, 1, 1)
    }

    fun show() {
        createDomain()
        createGrid()
        primaryStage.show()
    }
}
