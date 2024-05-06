package dev.zmeuion.vitalya.data

import dev.zmeuion.vitalya.database.DataStoreManager
import dev.zmeuion.vitalya.network.api.LoginApi


class LoginRepository(
    private val api: LoginApi,
    private val datastore: DataStoreManager,
) {

    suspend fun auth(username: String, login: String, password: String, type: String) {
        val token = api.auth(username, login, password, type)
        datastore.updateToken(token)
    }

    suspend fun getToken(): String {
        return datastore.getToken()
    }

    suspend fun dismissAuth() {
        datastore.updateToken("1")
    }

    suspend fun updateUsername(token: String) {
      datastore.updateUsername(api.getUsername(token))
    }
}