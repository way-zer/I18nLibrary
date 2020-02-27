package cf.wayzer.i18n

import cf.wayzer.i18n.util.OverlayMap

@Suppress("MemberVisibilityCanBePrivate")
object PlaceHoldManage {
    private val regex = Regex("\\{([^{}]*)}")
    val globalVars = mutableMapOf<String, Any>()
    fun replaceVars(str: String, vars: Map<String, Any>): String {
        val overlay = getOverlay(vars)
        return regex.replace(str) { match ->
            return@replace resolveVar(overlay[match.groupValues[1]], overlay)?.toString()
                    ?: match.value
        }
    }

    /**
     * resolveVar may recursive
     */
    fun resolveVar(v: Any?, vars: Map<String, Any>): Any? {
        return when (v) {
            is I18nSentence -> {
                v.copy(vars = OverlayMap(vars, v.vars)).toString()
            }
            is PlaceHoldHandler -> {
                v.handle {
                    resolveVar(vars[it], vars)
                }
            }
            else -> v
        }
    }

    fun getOverlay(vars: Map<String, Any>): Map<String, Any> {
        return OverlayMap(globalVars, vars)
    }
}
