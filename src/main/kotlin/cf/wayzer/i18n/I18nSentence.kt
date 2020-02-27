package cf.wayzer.i18n

@Suppress("MemberVisibilityCanBePrivate")
data class I18nSentence(
        private val defaultT: String,
        var vars: Map<String, Any> = emptyMap()
) {
    fun lang(string: String) = lang(I18nMain.getLang(string))

    fun lang(v: I18nLang): I18nSentence {
        addVars("_lang" to v)
        return this
    }

    fun addVars(vararg v: Pair<String, Any>): I18nSentence {
        vars = vars.plus(v)
        return this
    }

    val lang: I18nLang
        get() {
            val ret = when (val l = PlaceHoldManage.getOverlay(vars)["_lang"]) {
                is I18nLang -> return l
                is String -> I18nMain.getLang(l)
                else -> I18nLang.Default
            }
            lang(ret)
            return ret
        }

    val translatedTemplate: String get() = lang.getTranslate(defaultT)

    override fun toString(): String {
        return PlaceHoldManage.replaceVars(translatedTemplate, vars)
    }
}
