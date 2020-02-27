package cf.wayzer.i18n

interface I18nLang {
    val lang: String
    fun getTranslate(template: String): String
    class Impl(override val lang: String, private val caches: MutableMap<String, String>) : I18nLang {
        override fun getTranslate(template: String): String {
            return caches.getOrPut(template) {
                I18nMain.handleNotFind(lang, template)
                Default.getTranslate(template)
            }
        }
    }

    object Default : I18nLang {
        var displayName = "English"
        override val lang: String = "CODE_LANG"
        override fun getTranslate(template: String): String {
            if (template == lang) return displayName
            return template
        }
    }
}
