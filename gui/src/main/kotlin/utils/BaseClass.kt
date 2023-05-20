package utils // ktlint-disable filename
open class BaseClass() {
    companion object {
        private const val DEBUG = true

        internal fun log(msg: () -> String) {
            if (DEBUG) {
                println(msg())
                System.out.flush()
            }
        }
    }
}
