package endtoendexample

import core.Plan
import core.Planner
import domain.LogisticsDomain
import dsl.domain
import dsl.problem
import explanation.ContrastiveExplanationPresenter
import explanation.Explainer
import explanation.impl.QuestionReplaceOperator
import io.kotest.core.spec.style.AnnotationSpec

class EndToEndExampleLogistic : AnnotationSpec() {
    private val explainer = Explainer.of(Planner.strips())

    private val domain = domain {
        name = "logistic_world"
        types {
            +"anything"
            +"strings"("anything")
            +"locations"("strings")
            +"robots"("strings")
            +"containers"("strings")
        }
        predicates {
            +"connected"("locations", "locations")
            +"atLocation"("robots", "locations")
            +"loaded"("robots", "containers")
            +"unloaded"("robots")
            +"inContainerLocation"("containers", "robots")
        }
        actions {
            "move" {
                parameters {
                    "X" ofType "robots"
                    "Y" ofType "locations"
                    "Z" ofType "locations"
                }
                preconditions {
                    +"connected"("Y", "Z")
                    +"atLocation"("X", "Y")
                }
                effects {
                    +"atLocation"("X", "Z")
                    -"atLocation"("X", "Y")
                }
            }
            "load" {
                parameters {
                    "Z" ofType "locations"
                    "Y" ofType "containers"
                    "X" ofType "robots"
                }
                preconditions {
                    +"atLocation"("X", "Z")
                    +"inContainerLocation"("Y", "Z")
                }
                effects {
                    +"loaded"("X", "Y")
                    -"inContainerLocation"("Y", "Z")
                }
            }
            "unload" {
                parameters {
                    "Z" ofType "locations"
                    "Y" ofType "containers"
                    "X" ofType "robots"
                }
                preconditions {
                    +"atLocation"("X", "Z")
                    +"loaded"("X", "Y")
                }
                effects {
                    +"inContainerLocation"("Y", "Z")
                    -"loaded"("X", "Y")
                }
            }
        }
    }

    private val problem = problem(domain) {
        objects {
            +"robots"("r")
            +"locations"("l1", "l2", "l3", "l4", "l5", "l6", "l7")
            +"containers"("c1", "c2")
        }
        initialState {
            +"atLocation"("r", "l1")
            +"inContainerLocation"("c1", "l2")
            +"inContainerLocation"("c2", "l3")
            +"connected"("l1", "l2")
            +"connected"("l1", "l3")
            +"connected"("l2", "l4")
            +"connected"("l3", "l4")
            +"connected"("l4", "l5")
            +"connected"("l1", "l6")
            +"connected"("l5", "l6")
            +"connected"("l5", "l7")
            +"connected"("l1", "l5")
            +"connected"("l2", "l1")
            +"connected"("l3", "l1")
            +"connected"("l4", "l2")
            +"connected"("l4", "l3")
            +"connected"("l5", "l4")
            +"connected"("l6", "l2")
            +"connected"("l6", "l5")
            +"connected"("l7", "l5")
            +"connected"("l5", "l1")
        }
        goals {
            +"atLocation"("r", "l5")
            +"inContainerLocation"("c1", "l4")
            +"inContainerLocation"("c2", "l1")
        }
    }

    @Test
    fun replaceActionInStateLogisticDomain() {
        val formerPlan = Plan.of(
            listOf(
                LogisticsDomain.Operators.moveRfromL1toL3,
                LogisticsDomain.Operators.loadC2fromL3onR,
                LogisticsDomain.Operators.moveRfromL3toL1,
                LogisticsDomain.Operators.unloadC2fromRtoL1,
                LogisticsDomain.Operators.moveRfromL1toL2,
                LogisticsDomain.Operators.loadC1fromL2onR,
                LogisticsDomain.Operators.moveRfromL2toL4,
                LogisticsDomain.Operators.unloadC1fromRtoL4,
                LogisticsDomain.Operators.moveRfromL4toL5,
            ),
        )

        val question = QuestionReplaceOperator(
            problem,
            formerPlan,
            LogisticsDomain.Operators.moveRfromL4toL6,
            8,
            LogisticsDomain.States.alternativeState,
        )
        val explanation = explainer.explain(question)
        println(ContrastiveExplanationPresenter.of(explanation).present())
        println(ContrastiveExplanationPresenter.of(explanation).presentMinimalExplanation())
        println(ContrastiveExplanationPresenter.of(explanation).presentContrastiveExplanation())
    }
}
