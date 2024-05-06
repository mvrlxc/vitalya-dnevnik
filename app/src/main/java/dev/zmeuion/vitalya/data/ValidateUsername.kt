package dev.zmeuion.vitalya.data

import dev.zmeuion.vitalya.data.models.ValidationResult

class ValidateUsername {
    fun execute(username: String): ValidationResult {
        if (username.isBlank()) {
            return ValidationResult(false, "Имя не должно быть пустым")
        }

        if (username.length > 24) {
            return ValidationResult(false, "Максимальная длина 24 символа")
        }

        return ValidationResult(successful = true)
    }
}