package dev.zmeuion.vitalya.network.models

import kotlinx.serialization.Serializable

@Serializable
data class CommentDTO(
    val lessonID: Int,
    val username: String,
    val content: String,
    val sendingDateTime: String,
)