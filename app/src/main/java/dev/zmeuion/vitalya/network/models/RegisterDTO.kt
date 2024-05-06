package dev.zmeuion.vitalya.network.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDTO(
    val username: String,
    val login: String,
    val password: String,
)
