package dev.zmeuion.vitalya.network.models

import kotlinx.serialization.Serializable

@Serializable
data class GetToken(
    val token: String
)
