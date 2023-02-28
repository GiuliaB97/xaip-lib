package core

import core.impl.BinaryExpressionImpl

/**
 * Subtype of [Expression] which support operations with logic expression that apply an [Operand] to two [Expression].
 */
interface BinaryExpression : Expression {
    /**
     * @property expression1: first expression to be used.
     */
    val expression1: Expression

    /**
     * @property expression2: second expression to be used.
     */
    val expression2: Expression

    /**
     * @property operand: operand to be applied to the expressions.
     */
    val operand: String

    companion object {
        /**
         * Factory method for an [BinaryExpression] creation.
         */
        fun of(
            expression1: Expression,
            expression2: Expression,
            operand: String,
        ): BinaryExpression = BinaryExpressionImpl(expression1, expression2, operand)
    }
}
