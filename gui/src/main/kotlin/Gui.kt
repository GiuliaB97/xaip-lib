import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.stage.Stage


class Gui : Application() {

    override fun start(primaryStage: Stage) {
        val question = Label("Question type")
        val actionToRemove = Label("Action to be removed")
        val actionToAdd = Label("Action to be add")
        val position = Label("Position of the new action")
        val formerPlan = Label("Former plan")

        val tf1 = TextField()
        val tf2 = TextField()
        val tf3 = TextField()
        val tf4 = TextField()
        val tf5 = TextField()

        val Submit = Button("Submit")
        val root = GridPane()
        val scene = Scene(root, 400.0, 200.0)
        root.addRow(0, question, tf1)
        root.addRow(2, formerPlan, tf2)
        root.addRow(1, actionToRemove, tf3)
        root.addRow(3, actionToAdd, tf4)
        root.addRow(4, position, tf5)
        root.addRow(6, Submit)
        primaryStage.scene = scene
        primaryStage.show()
        /*
        val btn = Button()
        btn.text = "Say 'Hello World'"
        btn.onAction = EventHandler<ActionEvent> { println("Hello World!") }

        val root = StackPane()
        root.children.add(btn)

        val scene = Scene(root, 300.0, 250.0)

        if (primaryStage != null) {
            primaryStage.title = "Hello World!"
            primaryStage.scene = scene
            primaryStage.show()
        }

         */
    }
}

fun main(args: Array<String>) {
    Application.launch(Gui::class.java, *args)
}
