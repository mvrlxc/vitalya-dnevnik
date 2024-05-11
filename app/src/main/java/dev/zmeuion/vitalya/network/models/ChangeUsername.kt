package dev.zmeuion.vitalya.network.models

import kotlinx.serialization.Serializable



@Serializable
data class ChangeUsername(
    val token: String,
    val newUsername: String
)
