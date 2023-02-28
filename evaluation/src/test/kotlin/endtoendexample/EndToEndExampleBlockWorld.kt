package endtoendexample
import core.Plan
import core.Planner
import domain.BlockWorldDomain.Operators
import dsl.domain
import dsl.problem
import explanation.ContrastiveExplanationPresenter
import explanation.Explainer
import explanation.impl.QuestionAddOperator
import explanation.impl.QuestionPlanProposal
import io.kotest.core.spec.style.AnnotationSpec

class EndToEndExampleBlockWorld : AnnotationSpec() {
    private val explainer = Explainer.of(Planner.strips())

    private val formerPlan = Plan.of(
        listOf(
            Operators.unstackAB,
            Operators.putdownA,
            Operators.unstackCD,
            Operators.stackCA,
            Operators.pickD,
            Operators.stackDC,
            Operators.pickB,
            Operators.stackBD,
        ),
    )

    private val domain = domain {
        name = "block_world"
        types {
            +"anything"
            +"strings"("anything")
            +"blocks"("strings")
            +"locations"("strings")
        }
        predicates {
            +"at"("blocks", "locations")
            +"on"("blocks", "blocks")
            +"arm_empty"
            +"clear"("blocks")
        }
        actions {
            "pick" {
                parameters {
                    "X" ofType "blocks"
                }
                preconditions {
                    +"arm_empty"()
                    +"clear"("X")
                    +"at"("X", "floor")
                }
                effects {
                    +"at"("X", "arm")
                    -"arm_empty"
                    -"at"("X", "floor")
                    -"clear"("X")
                }
            }
            "stack" {
                parameters {
                    "X" ofType "blocks"
                    "Y" ofType "locations"
                }
                preconditions {
                    +"at"("X", "arm")
                    +"clear"("Y")
                }
                effects {
                    +"on"("X", "Y")
                    +"clear"("X")
                    +"arm_empty"
                    -"at"("X", "arm")
                    -"clear"("Y")
                }
            }

            "unstack" {
                parameters {
                    "X" ofType "blocks"
                    "Y" ofType "locations"
                }
                preconditions {
                    +"on"("X", "Y")
                    +"clear"("X")
                    +"arm_empty"
                }
                effects {
                    -"on"("X", "Y")
                    -"clear"("X")
                    -"arm_empty"
                    +"at"("X", "arm")
                    +"clear"("Y")
                }
            }
            "putdown" {
                parameters {
                    "X" ofType "blocks"
                }
                preconditions {
                    +"at"("X", "arm")
                    +"clear"("Y")
                }
                effects {
                    -"at"("X", "arm")
                    +"clear"("X")
                    +"arm_empty"
                    +"at"("X", "floor")
                }
            }
        }
    }

    private val problem = problem(domain) {
        objects {
            +"blocks"("a", "b", "c", "d")
            +"locations"("floor", "arm")
        }
        initialState {
            +"on"("a", "b")
            +"on"("c", "d")
            +"arm_empty"
            +"clear"("a")
            +"clear"("c")
            +"at"("b", "floor")
            +"at"("d", "floor")
        }
        goals {
            +"clear"("b")
            +"on"("b", "d")
            +"on"("d", "c")
            +"on"("c", "a")
            +"at"("a", "floor")
        }
    }

    @Test
    fun addOperatorBlockWorld() {
        val question = QuestionAddOperator(
            problem,
            formerPlan,
            Operators.putdownC,
            3,
        )
        val explanation = explainer.explain(question)
        println(ContrastiveExplanationPresenter.of(explanation).present())
        println(ContrastiveExplanationPresenter.of(explanation).presentMinimalExplanation())
        println(ContrastiveExplanationPresenter.of(explanation).presentContrastiveExplanation())
    }

    @Test
    fun planProposalBlockWorld() {
        val planProposal = Plan.of(
            listOf(
                Operators.unstackAB,
                Operators.putdownA,
                Operators.unstackCD,
                Operators.putdownC,
                Operators.pickC,
                Operators.stackCA,
                Operators.pickD,
                Operators.stackDC,
                Operators.pickB,
                Operators.stackBD,
            ),
        )
        val question = QuestionPlanProposal(problem, formerPlan, planProposal)
        val explanation = explainer.explain(question)

        println(ContrastiveExplanationPresenter.of(explanation).present())
        println(ContrastiveExplanationPresenter.of(explanation).presentMinimalExplanation())
        println(ContrastiveExplanationPresenter.of(explanation).presentContrastiveExplanation())
    }
}
