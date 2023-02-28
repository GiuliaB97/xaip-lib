package core

import core.impl.UnaryExpressionImpl

/**
 * Subtype of [Expression] which support operations with logic expression that apply an [Operand] to an [Expression].
 */

interface UnaryExpression : Expression {
    /**
     * @property expression: first expression to be used.
     */
    val expression: Expression

    /**
     * @property operand: operand to be applied to the expressions.
     */
    val operand: String

    companion object {
        /**
         * Factory method for an [UnaryExpression] creation.
         */
        fun of(
            expression: Expression,
            operand: String,
        ): UnaryExpression = UnaryExpressionImpl(expression, operand)
    }
}
