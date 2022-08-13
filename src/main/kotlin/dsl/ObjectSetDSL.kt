package dsl

import Object
import ObjectSet
import Type

/**
 *
 */
class ObjectSetDSL {
    var objectSet: ObjectSet = ObjectSet.of(emptyMap())

    /**
     *
     */
    operator fun Pair<Type, Set<Object>>.unaryPlus() {
        objectSet.map.plus(this)
    }

    /**
     * */
    operator fun String.invoke(vararg args: String): Pair<Type, Set<Object>> =
        Pair(
            Type.of(this),
            args.map {
                Object.of(it)
            }.toSet()
        )
}
