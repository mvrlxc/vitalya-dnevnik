package dev.zmeuion.vitalya.network.models

import kotlinx.serialization.Serializable

@Serializable
data class ChangePassword(
    val token: String,
    val newPassword: String,
    val oldPassword: String
)
