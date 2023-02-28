package explanation

import core.Plan
import core.State
import domain.BlockWorldDomain.Fluents
import domain.BlockWorldDomain.Operators
import domain.BlockWorldDomain.Problems
import explanation.impl.SimulatorImpl
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
class SimulatorTest : AnnotationSpec() {
    private val problem = Problems.armNotEmpty

    private val planPickB = Plan.of(listOf(Operators.pickB))
    private val planPickBstackBApickC = Plan.of(listOf(Operators.pickB, Operators.stackBA, Operators.pickC))
    private val simulationCorrect = SimulatorImpl().simulate(planPickB, problem.initialState)
    private val simulationCorrect2 = SimulatorImpl().simulate(planPickBstackBApickC, problem.initialState)

    @Test
    fun `Test simulation correct`() {
        simulationCorrect.first() shouldBe State.of(
            Fluents.atBArm,
            Fluents.atDFloor,
            Fluents.atCFloor,
            Fluents.atAFloor,
            Fluents.clearD,
            Fluents.clearC,
            Fluents.clearA,
        )
    }

    @Test
    fun `Test simulation correct2`() {
        simulationCorrect2.first() shouldBe State.of(
            Fluents.onBA,
            Fluents.atDFloor,
            Fluents.atCArm,
            Fluents.atAFloor,
            Fluents.clearD,
            Fluents.clearB,
        )
    }
}
