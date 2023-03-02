import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import javafx.stage.Stage

class View(private val primaryStage: Stage) {
    private val question = Label("Question type")
    private val actionToRemove = Label("Action to be removed")
    private val actionToAdd = Label("Action to add")
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

    private val vbox = VBox()
    private val insets = Insets(10.0)
    fun show() {
        val grid = GridPane()
        grid.alignment = Pos.CENTER
        grid.hgap = 10.0
        grid.vgap = 10.0
        grid.padding = Insets(25.0, 25.0, 25.0, 25.0)

        primaryStage.title = "xaip-lib-app"
        grid.add(question, 0, 1)
        grid.add(tf1, 1, 1)
        grid.add(formerPlan, 0, 2)
        grid.add(tf2, 1, 2)
        grid.add(actionToRemove, 0, 3)
        grid.add(tf3, 1, 3)
        grid.add(actionToAdd, 0, 4)
        grid.add(tf4, 1, 4)
        grid.add(position, 0, 5)
        grid.add(tf5, 1, 5)
        grid.add(newPlan, 0, 6)
        grid.add(tf6, 1, 6)
        grid.add(submit, 1, 7)

        val scene = Scene(grid, 400.0, 300.0)
        primaryStage.scene = scene

        primaryStage.show()
    }
}
