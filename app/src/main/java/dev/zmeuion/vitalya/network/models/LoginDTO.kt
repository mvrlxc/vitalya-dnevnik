package dev.zmeuion.vitalya.network.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginDTO(
    val login: String,
    val password: String,
)
