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
import Label.actionLabel
import Label.actionParameterLabel
import Label.domainLabel
import Label.formerPlanLabel
import Label.positionLabel
import Label.problemLabel
import Label.questionLabel
import Value.domainList
import Value.questionList
import core.Domain // ktlint-disable filename
import core.Problem
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.stage.Stage

object Label {
    val domainLabel = Label("Domain")
    val problemLabel = Label("Problem")
    val questionLabel = Label("Question type")
    val actionLabel = Label("")
    val positionLabel = Label("Position action")
    val formerPlanLabel = Label("Former plan")
    val actionParameterLabel = Label("Parameter")
}

object Value {
    lateinit var domain: Domain
    var problemList = emptyList<Problem>()
    lateinit var values: List<String>
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
    var problemComboBox = ComboBox(FXCollections.observableArrayList("                  ")) // by lazy { problems.forEach { emptyObservableList.add(it.name) }; ComboBox(emptyObservableList) }
    val questionComboBox = ComboBox(questionList)
    var actionComboBox = ComboBox(FXCollections.observableArrayList(""))
    var actionParameter1ComboBox = ComboBox(FXCollections.observableArrayList(""))
    var actionParameter2ComboBox = ComboBox(FXCollections.observableArrayList(""))
    var actionParameter3ComboBox = ComboBox(FXCollections.observableArrayList(""))

    val formerPlanTextField = TextField()
    val newPlanTextField = TextField()
    val actionPositionTextField = TextField()

    val submit = Button("Submit")
}

object GuiGrid {
    val grid = GridPane()

    fun initGrid(primaryStage: Stage): GridPane {
        grid.alignment = Pos.BASELINE_LEFT
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(25.0, 25.0, 25.0, 25.0)

        primaryStage.title = "xaip-lib-app"

        grid.add(domainLabel, 0, 1)
        grid.add(domainComboBox, 1, 1)

        grid.add(problemLabel, 0, 2)
        grid.add(problemComboBox, 1, 2)

        grid.add(questionLabel, 0, 3)
        grid.add(questionComboBox, 1, 3)

        grid.add(formerPlanLabel, 0, 4)
        grid.add(formerPlanTextField, 1, 4)

        grid.add(actionLabel, 0, 5)
        grid.add(actionComboBox, 1, 5)

        grid.add(actionParameterLabel, 0, 6)
        grid.add(actionParameter1ComboBox, 1, 6)
        grid.add(actionParameter2ComboBox, 2, 6)
        grid.add(actionParameter3ComboBox, 3, 6)

        grid.add(positionLabel, 0, 7)
        grid.add(actionPositionTextField, 1, 7)

        grid.add(newPlanTextField, 1, 5)

        grid.add(submit, 1, 9)

        return grid
    }
}

