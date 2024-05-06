package dev.zmeuion.vitalya.ui.screens.login

sealed class LoginEvent {
    data class UsernameChanged(val username: String) : LoginEvent()
    data class LoginChanged(val login: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data class ConfirmPasswordChanged(val repeatedPassword: String) : LoginEvent()

    data object ChangeAuthType : LoginEvent()

    data object Login : LoginEvent()

    data object Register : LoginEvent()

    data object DismissAuth : LoginEvent()
}