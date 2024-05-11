package dev.zmeuion.vitalya.data

import dev.zmeuion.vitalya.database.DataStoreManager
import dev.zmeuion.vitalya.network.api.LoginApi
import kotlinx.coroutines.flow.Flow

class OptionsRepository(
    private val dataStoreManager: DataStoreManager,
    private val api: LoginApi,
) {
    init {
        getUsername()
    }

    suspend fun editNotifications(input: Boolean) {
        dataStoreManager.updateNotifications(input)
    }

    suspend fun getNoti(): Boolean {
        return dataStoreManager.getNotifications()
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

    suspend fun getToken(): String {
        return dataStoreManager.getToken()
    }

    suspend fun updateUsername(username: String) {
        val token = getToken()
        api.changeUsername(token, username)
        dataStoreManager.updateUsername(username)
    }

    suspend fun updatePassword(newPassword: String, oldPassword: String): String {
        val token = getToken()
        return api.changePassword(token, oldPassword, newPassword)
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