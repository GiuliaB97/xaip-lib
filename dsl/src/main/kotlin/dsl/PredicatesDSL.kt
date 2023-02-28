package dsl

import core.Predicate
import core.Type
import dsl.provider.TypeProvider

/**
 * Class representing a [Predicate] in the DSL.
 */
class PredicatesDSL(private val typesProvider: TypeProvider) {
    /**
     * @property predicates: set of [Predicate] created.
     */
    val predicates = mutableSetOf<Predicate>()

    /**
     * Method that updates the internal list of [predicates] adding the last one created.
     */
    operator fun Predicate.unaryPlus() {
        if (typeExist(this.arguments)) predicates += this
    }

    /**
     * Method that create a [Predicate] from a [String] without arguments.
     */
    operator fun String.unaryPlus() {
        predicates += this()
    }

    /**
     * Method that checks if all the [types] are in [TypeProvider].
     */
    private fun typeExist(types: Iterable<Type>): Boolean {
        for (type in types)
            if (typesProvider.findType(type.name) == null) error("core.Type non found: ${type.name}")
        return true
    }

    /**
     * Method that takes some [String]s as parameters, converts that into [Type]s
     * and return a list contain all of them.
     */
    private fun typeConverter(vararg types: String): List<Type> {
        val typesList = mutableListOf<Type>()
        for (type in types) {
            val supertype = typesProvider.findType(type)?.superType
            typesList.add(Type.of(type, supertype))
        }
        return typesList
    }

    /**
     * Method that invoked on a [String] creates a [Predicate] from it and its argument.
     */
    operator fun String.invoke(vararg types: String): Predicate {
        val typesList = typeConverter(*types)
        if (typeExist(typesList)) {
            return Predicate.of(this, typesList)
        } else {
            error("This should never happen")
        }
    }
}
