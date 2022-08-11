import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import resources.TestUtils.Values
import resources.TestUtils.VariableAssignments

class VariableAssignmentTest : AnnotationSpec() {
    @Test
<<<<<<< HEAD
    fun testVariableAssignmentObjectY2XworksAsExpected() {
=======
    fun testVariableAssignmentObjectY2XWorksAsExpected() {
>>>>>>> aedfbaf0938b78efd7739b1d5fdecfacacbccf59
        VariableAssignments.y2x.values shouldBe arrayOf(Values.X)
        VariableAssignments.y2x.size shouldBe 1
        VariableAssignments.y2x.keys shouldBe arrayOf(Values.Y)
        VariableAssignments.y2x.isEmpty() shouldBe false
    }
<<<<<<< HEAD
=======

>>>>>>> aedfbaf0938b78efd7739b1d5fdecfacacbccf59
    @Test
    fun testVariableAssignmentObjectX2FloorWorksAsExpected() {
        VariableAssignments.x2floor.containsKey(Values.X) shouldBe true
        VariableAssignments.x2floor.containsValue(Values.floor) shouldBe true
        VariableAssignments.x2floor[Values.X] shouldBe Values.floor
        VariableAssignments.x2floor.merge(VariableAssignments.y2x) shouldBe VariableAssignment.of(
            Values.Y,
            Values.X
        ).merge(VariableAssignment.of(Values.X, Values.floor))
        VariableAssignments.x2floor.merge(VariableAssignments.x2arm) shouldBe VariableAssignment.empty()
    }
}
