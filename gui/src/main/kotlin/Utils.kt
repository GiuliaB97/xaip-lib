import Components.actionNameComboBox
import Components.actionParameter1ComboBox
import Components.actionParameter2ComboBox
import Components.actionParameter3ComboBox
import Components.actionPositionTextField
import Components.domainComboBox
import Components.explanationTextArea
import Components.explanationTypeComboBox
import Components.formerPlanTextField
import Components.newPlanTextField
import Components.problemNameComboBox
import Components.questionComboBox
import Components.stateTextField
import Components.submit
import Label.actionLabel
import Label.actionParameterLabel
import Label.domainLabel
import Label.explanationLabel
import Label.explanationTypeLabel
import Label.formerPlanLabel
import Label.positionLabel
import Label.problemLabel
import Label.questionLabel
import Label.stateLabel
import Value.domainList
import Value.explanationTypeList
import Value.questionList
import core.Domain // ktlint-disable filename
import core.Problem
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.* // ktlint-disable no-wildcard-imports
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage

object Label {
    val domainLabel = Label("Domain")
    val problemLabel = Label("Problem")
    val questionLabel = Label("Question type")
    val actionLabel = Label("")
    val positionLabel = Label("Position action")
    val formerPlanLabel = Label("Former plan")
    val actionParameterLabel = Label("Parameter")
    val explanationTypeLabel = Label("Explanation type")
    val stateLabel = Label("State")
    val explanationLabel = Label("Explanation")
}

object Value {
    lateinit var domain: Domain
    var problemList = emptyList<Problem>()
    lateinit var values: List<String>
    val explanationTypeList: ObservableList<String> = FXCollections.observableArrayList(
        "General",
        "Minimal",
    )
    var domainList: ObservableList<String> = FXCollections.observableArrayList(
        "Block world",
        "Logistics",
    )
    var questionList: ObservableList<String> = FXCollections.observableArrayList(
        "Question 1",
        "Question 2",
        "Question 3",
        "Question 4",
        "Question 5",
    )
}

object Components {
    val domainComboBox = ComboBox(domainList)
    val explanationTypeComboBox = ComboBox(explanationTypeList)
    var problemNameComboBox = ComboBox(FXCollections.observableArrayList(""))
    val questionComboBox = ComboBox(questionList)
    var actionNameComboBox = ComboBox(FXCollections.observableArrayList(""))
    var actionParameter1ComboBox = ComboBox(FXCollections.observableArrayList(""))
    var actionParameter2ComboBox = ComboBox(FXCollections.observableArrayList(""))
    var actionParameter3ComboBox = ComboBox(FXCollections.observableArrayList(""))

    val formerPlanTextField = TextField()
    val newPlanTextField = TextField()
    val stateTextField = TextField()
    val actionPositionTextField: Spinner<Int> = Spinner(0, 100, 0)

    var explanationTextArea = TextArea()

    val submit = Button("Submit")
}

object GuiGrid {
    private val grid = GridPane()
    private const val bigComboBoxMinDimension = 220.0

    private val hboxParameters = HBox()
    private val hboxExplanation = HBox()
    private val hboxMain = VBox()

    fun initGrid(primaryStage: Stage): VBox {
        grid.alignment = Pos.CENTER_LEFT
        grid.hgap = 5.0
        grid.vgap = 7.0
        grid.padding = Insets(25.0, 25.0, 25.0, 25.0)

        explanationTextArea.setPrefSize(650.0, 200.0)

        primaryStage.title = "xaip-lib-app"

        explanationTypeComboBox.maxWidth = bigComboBoxMinDimension
        domainComboBox.maxWidth = bigComboBoxMinDimension
        questionComboBox.maxWidth = bigComboBoxMinDimension
        actionNameComboBox.maxWidth = bigComboBoxMinDimension

        grid.add(explanationTypeLabel, 0, 1)
        grid.add(explanationTypeComboBox, 1, 1)

        grid.add(domainLabel, 0, 2)
        grid.add(domainComboBox, 1, 2)

        grid.add(problemLabel, 0, 3)
        grid.add(problemNameComboBox, 1, 3)

        grid.add(questionLabel, 0, 4)
        grid.add(questionComboBox, 1, 4)

        formerPlanTextField.maxWidth(bigComboBoxMinDimension)
        grid.add(formerPlanLabel, 0, 5)
        grid.add(formerPlanTextField, 1, 5)

        newPlanTextField.maxWidth(bigComboBoxMinDimension)
        grid.add(actionLabel, 0, 6)
        grid.add(actionNameComboBox, 1, 6)
        grid.add(newPlanTextField, 1, 6)

        grid.add(actionParameterLabel, 0, 7)
        hboxParameters.children.addAll(
            actionParameter1ComboBox,
            actionParameter2ComboBox,
            actionParameter3ComboBox,
        )
        hboxParameters.alignment = Pos.TOP_LEFT
        hboxParameters.spacing = 5.0

        grid.add(hboxParameters, 1, 7)
        grid.add(positionLabel, 0, 8)
        grid.add(actionPositionTextField, 1, 8)

        stateTextField.maxWidth(bigComboBoxMinDimension)
        grid.add(stateLabel, 0, 9)
        grid.add(stateTextField, 1, 9)

        grid.add(submit, 1, 10)

        grid.add(explanationLabel, 0, 11)
        hboxExplanation.children.addAll(explanationTextArea)
        hboxExplanation.alignment = Pos.CENTER
        hboxExplanation.spacing = 5.0
        // grid.add(hboxExplanation, 0, 12)
        hboxMain.children.addAll(grid, hboxExplanation)

        return hboxMain
    }
}
