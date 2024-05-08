package dev.zmeuion.vitalya.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.datastore: DataStore<Preferences> by preferencesDataStore("data_store")

class DataStoreManager(ctx: Context) {

    private val context = ctx

    private companion object {
        val token = stringPreferencesKey("token")
        val username = stringPreferencesKey("username")
        val schedule = stringPreferencesKey("schedule")
        val theme = stringPreferencesKey("theme")
    }

    suspend fun updateToken(tokenInput: String) {
        context.datastore.edit { datastore -> datastore[token] = tokenInput }
    }

    suspend fun getToken(): String {
        return context.datastore.data.first()[token] ?: ""
    }

    fun getTokenFlow(): Flow<String> {
        return context.datastore.data.map { it[token] ?: "" }
    }

    suspend fun updateUsername(usernameInput: String) {
        context.datastore.edit { datastore -> datastore[username] = usernameInput }
    }

    fun getUsernameFlow(): Flow<String?> {
        return context.datastore.data.map { it[username] }
    }

    suspend fun updateSchedule(scheduleInput: String) {
        context.datastore.edit { datastore -> datastore[schedule] = scheduleInput }
    }

    fun getScheduleFlow(): Flow<String> {
        return context.datastore.data.map { it[schedule] ?: "" }
    }

    suspend fun updateTheme(themeInput: String) {
        context.datastore.edit { datastore -> datastore[theme] = themeInput }
    }

    fun getThemeFlow(): Flow<String> {
        return context.datastore.data.map { it[theme] ?: "system" }
    }

    suspend fun getTheme(): String {
        return context.datastore.data.first()[theme] ?: "system"
    }

}