package dev.zmeuion.vitalya.network.api

import dev.zmeuion.vitalya.network.models.CommentDTO
import dev.zmeuion.vitalya.network.models.CreateComment
import dev.zmeuion.vitalya.network.models.GetToken
import dev.zmeuion.vitalya.network.models.ScheduleDTO
import dev.zmeuion.vitalya.network.models.TextDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CommentsApi(
    private val client: HttpClient
) {
    suspend fun getComments(id: Int): List<CommentDTO> {
        val body = TextDTO(text = id.toString())
        return client.post("/getComments") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body<List<CommentDTO>>()
    }

    suspend fun postComment(
        lessonID: Int,
        token: String,
        content: String,
        sendingDateTime: String,
    ): String {
        val body = CreateComment(
            lessonID = lessonID,
            token = token,
            content = content,
            sendingDateTime = sendingDateTime,
        )
        return client.post("/createComment") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body<TextDTO>().text
    }

}