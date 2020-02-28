package cf.wayzer.i18n

import java.io.File

object I18nFileHandler {
    /*Template
        #这是一个自定义文本格式
        # #开头为注释 ##开头为元数据或指令

        ##FROM
        Hello World
        ##TO
        你好 世界
        ##END
     */
    private enum class State {
        ReadFrom, ReadTo, End
    }

    fun readFrom(file: File): MutableMap<String, String> {
        val results = mutableMapOf<String, String>()
        val meteData = mutableListOf<String>() // For future use
        var state = State.End
        val builder = StringBuilder()
        var from = "ERROR"
        file.forEachLine { line ->
            when {
                line.startsWith("##") -> {
                    when (val meta = line.subSequence(2, line.length)) {
                        "FROM" -> {
                            assert(state == State.End)
                            state = State.ReadFrom
                            builder.clear()
                        }
                        "TO" -> {
                            assert(state == State.ReadFrom)
                            from = builder.toString()
                            state = State.ReadTo
                            builder.clear()
                        }
                        "END" -> {
                            assert(state == State.ReadTo)
                            results[from] = builder.toString()
                            state = State.End
                        }
                        else -> meteData.add(meta.toString())
                    }
                }
                line.startsWith("#") -> {
                }//PASS
                else -> builder.append(if (builder.isNotEmpty()) ("\n" + line) else line)
            }
        }
        return results
    }

    fun appendUnknown(file: File, temp: String) {
        val str = """
            |
            |##FROM
            |$temp
            |##TO
            |# This is created auto, you should change this to you want
            |$temp
            |##END
            |
        """.trimMargin()
        file.appendText(str)
    }
}
