package dsl

import core.BinaryExpression
import core.Fluent
import core.UnaryExpression
import domain.BlockWorldDomain.DomainsDSL
import domain.BlockWorldDomain.Predicates
import domain.BlockWorldDomain.Types
import domain.BlockWorldDomain.Values
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

class AxiomDSLTest : AnnotationSpec() {
    private val axioms = DomainsDSL.blockWorld.axioms

    @Test
    fun axiomDSLworksAsExpected() {
        axioms?.parameters?.keys?.size shouldBe 4
    }

    @Test
    fun axiomDSLparameters() {
        axioms?.parameters?.keys?.first()?.name?.let { it.filter { it.isUpperCase() } } shouldBe
            Values.X.name.filter { it.isUpperCase() }
        axioms?.parameters?.keys?.toList()?.get(1)?.name?.let { it.filter { it.isUpperCase() } } shouldBe
            Values.Y.name.filter { it.isUpperCase() }
        axioms?.parameters?.keys?.toList()?.get(2)?.name?.let { it.filter { it.isUpperCase() } } shouldBe
            Values.W.name.filter { it.isUpperCase() }
        axioms?.parameters?.keys?.toList()?.get(3)?.name?.let { it.filter { it.isUpperCase() } } shouldBe
            Values.Z.name.filter { it.isUpperCase() }

        axioms?.parameters?.values?.first()?.superType shouldBe Types.strings
        axioms?.parameters?.values?.toList()?.get(1)?.superType shouldBe Types.strings

        axioms?.parameters?.values?.toList()?.get(3)?.superType shouldBe Types.anything
        axioms?.parameters?.values?.toList()?.get(2)?.superType shouldBe null
    }

    @Test
    fun testContext() {
        val ctx: BinaryExpression = axioms?.context as BinaryExpression

        val binaryExpression = BinaryExpression.of(
            Fluent.of(Predicates.on, false, Values.a, Values.b),
            Fluent.of(Predicates.on, false, Values.a, Values.b),
            "or",
        )

        (ctx.expression1 as Fluent).name.filter { it.isLowerCase() } shouldBe
            (binaryExpression.expression1 as Fluent).name.filter { it.isLowerCase() }

        (ctx.expression2 as Fluent).name.filter { it.isLowerCase() } shouldBe
            (binaryExpression.expression2 as Fluent).name.filter { it.isLowerCase() }
    }

    @Test
    fun testImplies() {
        val implies: UnaryExpression = axioms?.implies as UnaryExpression

        val unaryExpression = UnaryExpression.of(
            Fluent.of(Predicates.on, false, Values.b, Values.c),
            "not",
        )
        (implies.expression as Fluent).name.filter { it.isLowerCase() } shouldBe
            (unaryExpression.expression as Fluent).name.filter { it.isLowerCase() }
    }
}
