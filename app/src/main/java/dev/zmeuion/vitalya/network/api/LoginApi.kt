package dev.zmeuion.vitalya.network.api

import dev.zmeuion.vitalya.network.models.GetToken
import dev.zmeuion.vitalya.network.models.LoginDTO
import dev.zmeuion.vitalya.network.models.RegisterDTO
import dev.zmeuion.vitalya.network.models.ScheduleDTO
import dev.zmeuion.vitalya.network.models.TextDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class LoginApi(
    private val client: HttpClient
) {

    suspend fun auth(username: String, login: String, password: String, type: String): String {
        val body = RegisterDTO(login = login, password = password, username = username)
        return client.post("/auth/$type") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body<TextDTO>().text

    }

    suspend fun getUsername(token: String): String {
        val body = GetToken(token = token)
        return client.post("/getUsername") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body<TextDTO>().text
    }

}