package dsl

import core.Fluent
import core.Object
import core.Variable
import dsl.provider.PredicateProvider

/**
 * Class containing a useful method to be shared among subclasses.
 * */
open class AbstractFluentDSL protected constructor(
    private val predicateProvider: PredicateProvider,
) {
    /**
     * Method that invoked on a [String] creates a [Fluent] from it and its arguments.
     */
    operator fun String.invoke(vararg args: String): Fluent =
        Fluent.of(
            predicateProvider.findPredicate(this, args.size)
                ?: error("Missing predicate: $this/${args.size}"),
            false,
            args.map {
                if (isNotEmpty() && it[0].isUpperCase()) {
                    Variable.of(it)
                } else {
                    Object.of(it)
                }
            },
        )
}
