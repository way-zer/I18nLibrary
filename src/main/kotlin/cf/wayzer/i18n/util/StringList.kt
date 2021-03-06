package cf.wayzer.i18n.util

/**
 * A normal [List] but [toString] is [joinToString]
 */
class StringList<T>(list: List<T>) : List<T> by list {
    override fun toString(): String {
        return joinToString("").dropLast(1)
    }
}
