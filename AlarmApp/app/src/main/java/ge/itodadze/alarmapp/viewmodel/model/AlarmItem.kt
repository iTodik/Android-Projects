package ge.itodadze.alarmapp.viewmodel.model

data class AlarmItem(val time: String, val active: Boolean) {

    companion object {
        const val ITEM_IDENTIFIER: String = "alarmItem"
        fun deserialize(strValue: String): AlarmItem? {
            if (strValue.length <= 4) return null
            return AlarmItem(strValue.substring(0, 2) + ":" + strValue.substring(2, 4),
                strValue.substring(4).toBoolean())
        }
    }

    fun serialize(): String {
        return time.substringBefore(":") + time.substringAfter(":") + active.toString()
    }

    fun getHour(): Int {
        return time.substringBefore(":").toInt()
    }

    fun getMinutes(): Int {
        return time.substringAfter(":").toInt()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is AlarmItem) {
            hashCode() == other.hashCode()
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return (getHour().toString() + getMinutes().toString()).toInt()
    }

}