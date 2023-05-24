package utils

import core.Domain // ktlint-disable filename
import core.Problem
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.* // ktlint-disable no-wildcard-imports
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import utils.Components.actionNameComboBox
import utils.Components.actionParameter1ComboBox
import utils.Components.actionParameter2ComboBox
import utils.Components.actionParameter3ComboBox
import utils.Components.actionPositionTextField
import utils.Components.domainComboBox
import utils.Components.explanationTextArea
import utils.Components.explanationTypeComboBox
import utils.Components.formerPlanTextField
import utils.Components.newPlanTextField
import utils.Components.problemNameComboBox
import utils.Components.questionComboBox
import utils.Components.stateTextField
import utils.Components.submit
import utils.Label.actionLabel
import utils.Label.actionParameterLabel
import utils.Label.domainLabel
import utils.Label.explanationLabel
import utils.Label.explanationTypeLabel
import utils.Label.fontLabel
import utils.Label.formerPlanLabel
import utils.Label.positionLabel
import utils.Label.problemLabel
import utils.Label.questionLabel
import utils.Label.stateLabel
import utils.Value.domainList
import utils.Value.explanationTypeList
import utils.Value.questionList

object Label {
    val fontLabel: Font = Font.font(/*"verdana",*/null, FontWeight.SEMI_BOLD, FontPosture.REGULAR, 12.5)
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
    private const val spacingHBox = 5.0
    private const val insectsBorderGrid = 25.0
    private const val vGapGrid = 7.0
    private const val hGapGrid = 5.0
    private const val prefHeightGrid = 650.0
    private const val prefWidthGrid = 200.0

    private val posGrid = Pos.CENTER_LEFT

    private var labelsList = listOf(
        actionLabel, explanationLabel, explanationTypeLabel,
        domainLabel, problemLabel, actionParameterLabel,
        formerPlanLabel, positionLabel, questionLabel,
        stateLabel,
    )

    /**
     * returns a grid initialize with provided values.
     */
    fun getGrid() = initGrid(posGrid, prefWidthGrid, prefHeightGrid, hGapGrid, vGapGrid, insectsBorderGrid)

    private fun initHBox(pos: Pos, spacing: Double, children: List<Node>): HBox {
        val hbox = HBox()
        hbox.alignment = pos
        hbox.spacing = spacing
        for (child in children) {
            hbox.children.add(
                child,
            )
        }
        return hbox
    }

    private fun initGrid(
        pos: Pos,
        prefWidth: Double,
        prefHeight: Double,
        hGap: Double,
        vGap: Double,
        insectsBorderGrid: Double,
    ): VBox {
        labelsList = initLabel(labelsList, fontLabel)
        grid.alignment = pos
        grid.hgap = hGap
        grid.vgap = vGap
        grid.padding = Insets(insectsBorderGrid, insectsBorderGrid, insectsBorderGrid, insectsBorderGrid)

        explanationTypeLabel.font = fontLabel
        explanationTextArea.setPrefSize(prefHeight, prefWidth)

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

        val hBoxParameters = initHBox(
            Pos.TOP_LEFT,
            spacingHBox,
            listOf(actionParameter1ComboBox, actionParameter2ComboBox, actionParameter3ComboBox),
        )

        grid.add(hBoxParameters, 1, 7)
        grid.add(positionLabel, 0, 8)
        grid.add(actionPositionTextField, 1, 8)

        stateTextField.maxWidth(bigComboBoxMinDimension)
        grid.add(stateLabel, 0, 9)
        grid.add(stateTextField, 1, 9)

        grid.add(submit, 1, 10)

        grid.add(explanationLabel, 0, 11)
        val hBoxExplanation = initHBox(Pos.CENTER, spacingHBox, listOf(explanationTextArea))
        val hBoxMain = VBox()
        hBoxMain.children.addAll(grid, hBoxExplanation)

        return hBoxMain
    }

    @Suppress("UNCHECKED_CAST")
    private fun initLabel(labels: List<Label>, font: Font): List<Label> {
        for (label in labels) {
            label.font = font
        }
        return labels
    }
}
