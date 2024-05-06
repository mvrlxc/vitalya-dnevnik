package dev.zmeuion.vitalya.data.models

data class ValidationResult(
    val successful: Boolean,
    val error: String? = null,
)
