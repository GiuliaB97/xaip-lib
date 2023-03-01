import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.stage.Stage

class View(private val primaryStage: Stage) {
    private val question = Label("Question type")
    private val actionToRemove = Label("Action to be removed")
    private val actionToAdd = Label("Action to be add")
    private val position = Label("Position of the new action")
    private val formerPlan = Label("Former plan")
    private val newPlan = Label("New plan")

    private val tf1 = TextField()
    private val tf2 = TextField()
    private val tf3 = TextField()
    private val tf4 = TextField()
    private val tf5 = TextField()
    private val tf6 = TextField()

    private val submit = Button("Submit")
    private val root = GridPane()
    private val scene = Scene(root, 400.0, 200.0)

    fun show() {
        root.addRow(0, question, tf1)
        root.addRow(2, formerPlan, tf2)
        root.addRow(1, actionToRemove, tf3)
        root.addRow(3, actionToAdd, tf4)
        root.addRow(4, position, tf5)
        root.addRow(5, newPlan, tf6)
        root.addRow(6, submit)
        primaryStage.scene = scene
        primaryStage.show()
    }
}
