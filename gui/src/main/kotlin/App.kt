import javafx.application.Application
import javafx.stage.Stage

/***
 * Class responsible for the creation of the application.
 */
class App : Application() {

    /**
     * Method responsible for the creation of the main components of the application.
     */
    override fun start(primaryStage: Stage) {
        val controller = Controller()
        val view = View(primaryStage, controller)
        view.show()
    }
}

/***
 * Entry point for the application.
 */
@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    Application.launch(App::class.java, *args)
}
