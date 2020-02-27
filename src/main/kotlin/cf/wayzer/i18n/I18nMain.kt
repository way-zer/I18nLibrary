package cf.wayzer.i18n

import java.nio.file.Path
import java.util.logging.Logger

object I18nMain {
    private var iRootDir: Path? = null
    var rootDir: Path
        get() = iRootDir ?: throw Error("Not init rootDir")
        set(value) {
            if (iRootDir == null) iRootDir = value
            else throw Error("root Dir has init")
        }

    private val allLang = mutableMapOf<String, I18nLang>(I18nLang.Default.lang to I18nLang.Default)

    fun getLang(lang: String): I18nLang {
        return allLang.getOrPut(lang) {
            I18nApi.loadLang(lang) ?: I18nLang.Default
        }
    }

    fun resetCache() {
        allLang.clear()
        allLang[I18nLang.Default.lang] = I18nLang.Default
    }

    fun handleNotFind(lang: String, template: String) {
        Logger.getLogger("i18n").warning("[i18n]Not Find translation in '$lang' for:$template")
        if (I18nApi.autoAppendForUnknown) I18nFileHandler.appendUnknown(rootDir.resolve("lang-$lang.lang").toFile(), template)
    }
}
