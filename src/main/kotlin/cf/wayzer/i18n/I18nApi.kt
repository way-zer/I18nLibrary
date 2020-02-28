package cf.wayzer.i18n

import cf.wayzer.i18n.I18nApi.toString
import java.nio.file.Path

@Suppress("MemberVisibilityCanBePrivate", "unused")
object I18nApi {
    /**
     * Init lang Dir
     * Must before use
     */
    fun init(root: Path) {
        I18nMain.rootDir = root
        registerGlobalVal("lang", object : PlaceHoldHandler {
            override fun handle(getOther: (String) -> Any?): String? {
                return (getOther("_lang")!! as I18nLang).let { it.getTranslate(it.lang) }
            }
        })
    }

    /**
     * Set [I18nLang.Default]'s displayName
     */
    fun setDisplayName(name: String) {
        I18nLang.Default.displayName = name
    }

    /**
     * Main api, get string after i18n and vars
     * Must after [init]
     * @param args vars Support [String] [I18nSentence] [PlaceHoldHandler] [Iterable], others will call [toString]
     */
    fun String.i18n(vararg args: Pair<String, Any>) = I18nSentence(this, args.toMap())

    /**
     * For java
     * @param vars vars Support [String] [I18nSentence] [PlaceHoldHandler] [Iterable], others will call [toString]
     */
    fun i18n(template: String, vars: Map<String, Any>) = I18nSentence(template, vars)

    /**
     * Reset all Lang cache
     */
    fun resetCache() = I18nMain.resetCache()

    /** default seen as [String]
     *  dynamic use handler [PlaceHoldHandler]
     *  @param value Support [String] [I18nSentence] [PlaceHoldHandler] [Iterable], others will call [toString]
     */
    fun registerGlobalVal(key: String, value: Any) {
        PlaceHoldManage.globalVars[key] = value
    }

    /**
     * Provider vars for all type [T] var
     * usage: {a.x} typeof a is [T], x is provider by [TypeVariableProvider]
     */
    inline fun <reified T : Any> registerTypeProvider(provider: TypeVariableProvider<T>) {
        PlaceHoldManage.typeProviders[T::class] = provider
    }

    var loadLang: (String) -> I18nLang? = {
        val file = I18nMain.rootDir.resolve("lang-$it.lang").toFile()
        if (file.exists() && file.isFile) {
            val data = I18nFileHandler.readFrom(file)
            I18nLang.Impl(it, data)
        } else null
    }
    var autoAppendForUnknown = true
}
