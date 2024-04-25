package dev.zmeuion.vitalya.network.api

import dev.zmeuion.vitalya.network.models.LoginDTO
import dev.zmeuion.vitalya.network.models.ScheduleDTO
import dev.zmeuion.vitalya.network.models.TextDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ScheduleApi(
    private val client: HttpClient
) {

    suspend fun getSchedule(): List<ScheduleDTO> {
        return client.get("/schedule") {
            contentType(ContentType.Application.Json)
        }.body<List<ScheduleDTO>>()
    }

}