package cf.wayzer.i18n

interface PlaceHoldHandler {
    /**
     * WARN: Can't cycle depending
     */
    fun handle(getOther: (String) -> Any?): Any?
}
