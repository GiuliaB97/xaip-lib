import javafx.application.Application
import javafx.stage.Stage

/***
 * .
 */
class App : Application() {

    override fun start(primaryStage: Stage) {
        // val model = Model()
        // val controller = Controller()
        val view = View(primaryStage)
        view.show()
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

/***
 * .
 */
fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}
