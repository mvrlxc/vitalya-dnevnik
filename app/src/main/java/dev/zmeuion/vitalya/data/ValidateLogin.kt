package dev.zmeuion.vitalya.data

import dev.zmeuion.vitalya.data.models.ValidationResult

class ValidateLogin {

    fun execute(login: String): ValidationResult {
        if (login.isBlank()) {
            return ValidationResult(false, "Логин не должен быть пустым")
        }

        if (login.length < 6) {
            return ValidationResult(false, "Минимальная длина 6 символов")
        }

        if (login.length > 24) {
            return ValidationResult(false, "Максимальная длина 24 символа")
        }

        if (!isAlphanumeric(login)) {
            return ValidationResult(false, "Логин может содержать только цифры и английские буквы")
        }

        if (!isAlpha(login)){
            return ValidationResult(false, "Логин должен начинаться с буквы")
        }

        return ValidationResult(successful = true)
    }

    private fun isAlphanumeric(text: String): Boolean {
        val pattern = Regex("[A-Za-z0-9]*")
        return pattern.matches(text)
    }

    private fun isAlpha(text: String): Boolean {
        val textInput = text[0]
        val pattern = Regex("[A-Za-z]*")
        return pattern.matches(textInput.toString())
    }


}