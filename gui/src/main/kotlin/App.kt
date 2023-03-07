import javafx.application.Application
import javafx.stage.Stage

/***
 * .
 */
class App : Application() {

    override fun start(primaryStage: Stage) {
        // val model = Model()
        val controller = Controller()
        val view = View(primaryStage, controller)
        view.show()
    }
}

/***
 * .
 */
fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}
