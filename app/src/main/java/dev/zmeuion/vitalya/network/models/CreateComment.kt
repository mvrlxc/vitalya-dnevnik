package dev.zmeuion.vitalya.network.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateComment(
    val lessonID: Int,
    val token: String,
    val content: String,
    val sendingDateTime: String,
)
