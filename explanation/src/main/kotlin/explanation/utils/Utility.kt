package explanation.utils

import core.* // ktlint-disable no-wildcard-imports

internal fun findAction(inputOperator: Operator, actionList: Iterable<Action>): Action =
    actionList.first { it.name == inputOperator.name }

private fun Set<Fluent>.conditionMatch(conditions: Set<Effect>) =
    this.all { fluent1 ->
        conditions.any { effect ->
            effect.fluent.match(fluent1)
        }
    }

internal fun Operator.isIdempotentOperators(operator: Operator): Boolean =
    this.preconditions.conditionMatch(operator.effects) &&
        operator.preconditions.conditionMatch(this.effects) &&
        this.args.all { operator.args.contains(it) }

/**
 *
 */
fun List<Operator>.retrieveArtificialOperator() =
    this.filter { it.name.contains("^") }.getOrNull(0)

/**
 * returns the actions corresponding to the given [operator].
 */
fun Set<Action>.retrieveAction(operator: Operator) =
    this.first {
        it.name == operator.name.filter { char -> char.isLetter() }
    }

private fun createInitialOperator(action: Action, operator: Operator): Operator {
    var newOperator = Operator.of(action)
    for (arg in operator.args) {
        newOperator = newOperator.apply(
            VariableAssignment.of(
                operator.parameters.keys.toList()[operator.args.indexOf(arg)],
                arg,
            ),
        )
    }
    return newOperator
}

/**
 * replace the operator created during the compilation process with the respective one in the original domain.
 */
fun List<Operator>.replaceArtificialOperator(actionList: Set<Action>): List<Operator> {
    val newList = mutableListOf<Operator>()
    this.toMutableList().map { operator ->
        if ("^" in operator.name) {
            newList.add(createInitialOperator(actionList.retrieveAction(operator), operator))
        } else {
            newList.add(operator)
        }
    }
    return newList
}
