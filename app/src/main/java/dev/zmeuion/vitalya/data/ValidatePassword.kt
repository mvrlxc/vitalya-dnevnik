package dev.zmeuion.vitalya.data

import dev.zmeuion.vitalya.data.models.ValidationResult

class ValidatePassword {
    fun execute(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(false, "Пароль не должен быть пустым")
        }

        if (password.length > 24) {
            return ValidationResult(false, "Максимальная длина 24 символа")
        }

        if (password.length < 6) {
            return ValidationResult(false, "Минимальная длина 6 символов")
        }

        if (!isAlphanumericAndSymbol(password)){
            return ValidationResult(false, "Пароль может содержать только цифры, символы и английские буквы")
        }

        return ValidationResult(successful = true)
    }

    private fun isAlphanumericAndSymbol(text: String): Boolean {
        val pattern = Regex("[A-Za-z0-9!@#\$%^&*()-_=+\\\\|\\[\\]{};:'\",.<>/?]*")
        return pattern.matches(text)
    }

}