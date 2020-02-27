# 一个支持动态扩充的国际化库(A library for i18n in dynamic with strong variable supporting)
采用kotlin编写,可运行在jvm平台  
Write in kotlin, can use on jvm platform
## 功能(Features)
- [x] 文本国际化(Simple text support)
- [x] 变量替换(text variable)
- [x] 全局变量处理(global variable and provider)
- [x] 嵌套变量处理(nested variable replacement)
- [ ] 时间日期本地化(i18n for time and date)
## 快速开始(Quick Start)
```kotlin
    // Note: 假设已配置中文翻译文件(Assert there are 'zh' language file)
    import cf.wayzer.i18n.I18nApi
    import cf.wayzer.i18n.I18nApi.i18n
    // 初始化语言文件目录(init lang dir)
    I18nApi.init(Paths.get("lang"))
    // 以默认语言输出,即代码内语言 (print in default language,language in code)
    println("Hello World".i18n()) //OUT: Hello World
    // 变量支持 (with variable)
    println("Hello {v}".i18n("v" to "World")) //OUT: Hello World
    // 切换语言 (with language)
    println("Hello World".i18n().lang("zh")) //OUT: 你好 世界
    
    // 使用内部变量(use intern var 'lang')
    println("lang is {lang}".i18n().lang("zh"))//OUT: 当前语言是中文
    
    // 添加全局变量处理器(add global place_hold_handler)
    I18nApi.registerGlobalVal("other_var", object : PlaceHoldHandler {
        override fun handle(getOther: (String) -> Any?): String? {
            // 这里依赖了变量"lang" (depend on a variable "lang")
            return getOther("lang")?.toString()
        }
    })
    // Note: 假设翻译文件中并没有该句(Assert no translation in 'zh')
    println("other_var is {other_var}".i18n().lang("zh")) //OUT: other_var is zh 
    
    // 复杂嵌套语句(complex nested use)
    val nest1 = "Here is Chinese {nest2}".i18n()
    val nest2 = "lang is {lang}".i18n().lang(I18nLang.Default)
    println("nest Test '{nest1}' '{nest2}'".i18n("nest1" to nest1, "nest2" to nest2).lang("zh"))
    //OUT: 嵌套测试: '这里将是中文 lang is CODE_LANG' 'lang is CODE_LANG'
```
## 翻译文件格式(language file format)
```
# 这是一个自定义格式
# #开头为注释 ##开头为元数据或指令

##FROM
Hello World
##TO
你好 世界
##END
```
在遇到未知字段时,会在文件末端添加  
When encountering unknown text, it will add to the file's end

## 变量
所有的不用于展示的变量应当以'\_'开头,否则可视为String,处理时推荐使用'?.'  
All variables not using for display should begin with '\_'. Others can
be seen as String. Recommend using '?.' on nullable var.

| Name | Note |
|:---|:---|
| _lang | I18nLang Object (对象) |
| lang | 当前语言展示名(lang name for display) |
