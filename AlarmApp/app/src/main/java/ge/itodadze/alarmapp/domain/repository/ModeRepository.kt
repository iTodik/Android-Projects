package ge.itodadze.alarmapp.domain.repository

interface ModeRepository {
    fun switch(): Boolean
    fun get(): Boolean
}