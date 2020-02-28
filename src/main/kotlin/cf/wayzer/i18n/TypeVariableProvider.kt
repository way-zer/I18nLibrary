package cf.wayzer.i18n

/**
 * Provider vars for all type [T] variable
 * use [registerVar] in init{} or override [resolveChild] for dynamic key
 */
open class TypeVariableProvider<T : Any> {
    private val allVar = mutableMapOf<String, (T) -> Any?>()
    fun registerVar(name: String, f: (T) -> Any?) {
        allVar[name] = f
    }

    open fun resolveChild(obj: T, key: String): Any? {
        return allVar[key]?.invoke(obj)
    }
}
