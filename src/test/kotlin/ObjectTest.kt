import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkClass
import org.junit.Test

class ObjectTest {
    private val nameGC ="Giovanni"
    private val objEmpty = mockkClass(Object::class) {
        every { representation } returns ""
    }
    private val objNotEmpty = mockkClass(Object::class) {
        every { representation } returns nameGC
    }
    @Test
    fun testEmptyCreation() {
        objEmpty.representation shouldBe ""
    }

    @Test
    fun testNotEmptyCreation() {
        objNotEmpty.representation shouldBe nameGC
    }
}