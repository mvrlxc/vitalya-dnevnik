package dev.zmeuion.vitalya.data

import dev.zmeuion.vitalya.database.DataStoreManager
import kotlinx.coroutines.flow.Flow

class OptionsRepository(
    private val dataStoreManager: DataStoreManager
) {
    init {
        getUsername()
    }

    fun getUsername(): Flow<String?> {
        return dataStoreManager.getUsernameFlow()
    }

    suspend fun deleteToken() {
        dataStoreManager.updateToken("")
    }

    suspend fun deleteUsername() {
        dataStoreManager.updateUsername("")
    }

    suspend fun updateUsername(username: String) {
        dataStoreManager.updateUsername(username)
    }

    suspend fun updateSchedule(schedule: String) {
        dataStoreManager.updateSchedule(schedule)
    }

    fun getSchedule(): Flow<String> {
        return dataStoreManager.getScheduleFlow()
    }

    fun getThemeFlow(): Flow<String> {
        return dataStoreManager.getThemeFlow()
    }

    suspend fun getTheme(): String {
        return dataStoreManager.getTheme()
    }

    suspend fun updateTheme(thme: String) {
        dataStoreManager.updateTheme(thme)
    }


}