package ge.itodadze.alarmapp.domain.repository.implementation

import android.content.SharedPreferences
import ge.itodadze.alarmapp.domain.repository.ModeRepository

class SharedModeRepository(
    initialMode: Boolean,
    private val sharedPreferences: SharedPreferences): ModeRepository {

    companion object {
        private const val INITIALIZED: String = "initialized"
        private const val MODE: String = "mode"
    }

    init {
        val initialized: Boolean = sharedPreferences.getBoolean(INITIALIZED, false)
        if (!initialized) {
            sharedPreferences.edit()
                .putBoolean(MODE, initialMode)
                .putBoolean(INITIALIZED, true)
                .apply()
        }
    }

    override fun switch(): Boolean {
        val prev: Boolean = sharedPreferences.getBoolean(MODE, false)
        sharedPreferences.edit().putBoolean(MODE, !prev).apply()
        return !prev
    }

    override fun get(): Boolean {
        return sharedPreferences.getBoolean(MODE, false)
    }

}