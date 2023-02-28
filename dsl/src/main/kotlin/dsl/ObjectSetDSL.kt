package dsl

import core.Object
import core.ObjectSet
import core.Type
import dsl.provider.TypeProvider

/**
 *
 */
class ObjectSetDSL(private val typeProvider: TypeProvider) {
    /**
     * @property objectSet: represents the objects created.
     */
    var objectSet: ObjectSet = ObjectSet.of(emptyMap())

    /**
     * @property map: map, which associate each [Object] to the corresponding [Type].
     */
    var map = emptyMap<Type, Set<Object>>()

    /**
     * Method that updates the internal list of [objectSet] adding the last one created.
     */
    operator fun Pair<Type, Set<Object>>.unaryPlus() {
        objectSet.map = objectSet.map.plus(this)
    }

    /**
     * Method that invoked on a [String] creates a [Pair] from it and its arguments.
     */
    operator fun String.invoke(vararg args: String): Pair<Type, Set<Object>> {
        val type = typeProvider.findType(this)
        if (type != null) {
            return Pair(
                type,
                args.map {
                    Object.of(it)
                }.toSet(),
            )
        } else {
            error("type not present in the core.Domain definition")
        }
    }
}
