package explanation

import explanation.impl.ContrastiveExplanationPresenterImpl

interface ContrastiveExplanationPresenter : ExplanationPresenter {
    /**
     * Explainer for contrastive explanations.
     * */
    fun presentContrastiveExplanation(): String

    companion object {
        /**
         * Factory method for an [ContrastiveExplanationPresenter] creation.
         */
        fun of(
            explanation: Explanation,
        ): ContrastiveExplanationPresenter = ContrastiveExplanationPresenterImpl(explanation)
    }
}
