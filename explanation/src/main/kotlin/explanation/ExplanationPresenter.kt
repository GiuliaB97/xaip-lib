package explanation

import explanation.impl.BaseExplanationPresenter

/**
 *
 */
interface ExplanationPresenter {
    val explanation: Explanation

    /**
     * devises a general explanation for the given [explanation].
     */
    fun present(): String

    /**
     * devises a minimal version of the general explanation for the given [explanation].
     */
    fun presentMinimalExplanation(): String
    companion object {
        /**
         * Factory method for an [Explanation] creation.
         */
        fun of(
            explanation: Explanation,
        ): ExplanationPresenter = BaseExplanationPresenter(explanation)
    }
}
