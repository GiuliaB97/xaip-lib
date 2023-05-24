package utils // ktlint-disable filename
object Debugger {
    private const val DEBUG = true

    internal fun log(msg: () -> String) {
        if (DEBUG) {
            println(msg())
            System.out.flush()
        }
    }
}
