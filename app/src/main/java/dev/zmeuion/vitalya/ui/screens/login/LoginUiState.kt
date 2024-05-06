package dev.zmeuion.vitalya.ui.screens.login

data class LoginUiState(
    val username: String = "",
    val usernameError: String? = null,
    val login: String = "",
    val loginError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirm: String = "",
    val confirmError: String? = null,
    val currentAuthType: Boolean = false,
    val isLoading: Boolean = false,
)