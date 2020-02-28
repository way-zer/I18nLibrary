import cf.wayzer.i18n.I18nApi
import cf.wayzer.i18n.I18nApi.i18n
import cf.wayzer.i18n.I18nLang
import cf.wayzer.i18n.PlaceHoldHandler
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import java.nio.file.Paths

class Test {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            I18nApi.init(Paths.get("src/test/resources/lang"))
            I18nApi.autoAppendForUnknown = false
            I18nApi.setDisplayName("Lang-in-Code")
        }
    }

    @Test
    fun printDefault() {
        Assert.assertEquals("Hello World", "Hello World".i18n().toString())
    }

    @Test
    fun testVars() {
        Assert.assertEquals("Hello World", "Hello {v}".i18n("v" to "World").toString())
    }

    @Test
    fun notFound() {
        Assert.assertEquals("NOT FOUND", "NOT FOUND".i18n().lang("zh").toString())
    }

    @Test
    fun translate() {
        Assert.assertEquals("你好 World", "Hello {v}".i18n("v" to "World").lang("zh").toString())
    }

    @Test
    fun transUsingVar() {
        Assert.assertEquals("你好 World", "Hello {v}".i18n("v" to "World").addVars("_lang" to "zh").toString())
    }

    @Test
    fun speedTest() {
        Assert.assertEquals("数字 0 88 99", "num {num} {v2} {v3}".i18n("num" to 0, "v2" to 88, "v3" to 99).lang("zh").toString())
        val start = System.nanoTime()
        (1..10000).forEach {
            if ("数字 $it 88 99" != "num {num} {v2} {v3}".i18n("num" to it, "v2" to 88, "v3" to 99).lang("zh").toString())
                error("NOT SAME")
        }
        val end = System.nanoTime()
        println(end - start)
    }

    @Test
    fun globalVar() {
        Assert.assertEquals("当前语言是中文", "lang is {lang}".i18n().lang("zh").toString())
    }

    @Test
    fun nested() {
        val nest1 = "Here is Chinese {nest2}".i18n()
        val nest2 = "lang is {lang}".i18n().lang(I18nLang.Default)
        Assert.assertEquals("嵌套测试: '这里将是中文 lang is Lang-in-Code' 'lang is Lang-in-Code'",
                "nest Test '{nest1}' '{nest2}'".i18n("nest1" to nest1, "nest2" to nest2).lang("zh").toString())
    }

    @Test
    fun nestedVar() {
        I18nApi.registerGlobalVal("nestVar", object : PlaceHoldHandler {
            override fun handle(getOther: (String) -> Any?): String? {
                return getOther("lang")?.toString()
            }
        })
        Assert.assertEquals("Lang-in-Code", "{nestVar}".i18n().toString())
    }
}
