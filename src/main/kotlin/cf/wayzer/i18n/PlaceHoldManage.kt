package cf.wayzer.i18n

import cf.wayzer.i18n.util.OverlayMap
import cf.wayzer.i18n.util.StringList
import kotlin.reflect.KClass

@Suppress("MemberVisibilityCanBePrivate")
object PlaceHoldManage {
    private val regex = Regex("\\{([^{}]*)}")
    val globalVars = mutableMapOf<String, Any>()
    val typeProviders = mutableMapOf<KClass<out Any>, TypeVariableProvider<out Any>>()

    fun replaceVars(str: String, vars: Map<String, Any>): String {
        val overlay = getOverlay(vars)
        return regex.replace(str) { match ->
            resolveVar(match.groupValues[1], overlay)?.toString() ?: match.value
        }
    }

    /**
     * getVarByName
     */
    fun resolveVar(v: String, vars: Map<String, Any>): Any? {
        resolveVarValue(vars[v], vars)?.let { return it }
        val sp = v.split(".")
        if (sp.size > 1) {
            var obj: Any? = resolveVarValue(vars[sp[0]], vars)
            var i = 1
            while (i < sp.size && obj != null && typeProviders.containsKey(obj::class)) {
                @Suppress("UNCHECKED_CAST")
                obj = resolveVarValue((typeProviders[obj::class] as? TypeVariableProvider<Any>)?.resolveChild(obj, sp[i]), vars)
                i++
            }
            return obj
        }
        return null
    }

    /**
     * resolveValue may recursive
     */
    fun resolveVarValue(v: Any?, vars: Map<String, Any>): Any? {
        return when (v) {
            is I18nSentence -> {
                v.copy(vars = OverlayMap(vars, v.vars)).toString()
            }
            is PlaceHoldHandler -> {
                v.handle {
                    resolveVar(it, vars)
                }
            }
            is Iterable<*> -> {
                StringList(v.map { resolveVarValue(it, vars) })
            }
            else -> v
        }
    }

    fun getOverlay(vars: Map<String, Any>): Map<String, Any> {
        return OverlayMap(globalVars, vars)
    }
}
