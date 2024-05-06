package dev.zmeuion.vitalya.ui.screens.login

import android.content.res.Resources.NotFoundException
import android.net.http.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zmeuion.vitalya.data.LoginRepository
import dev.zmeuion.vitalya.data.ValidateLogin
import dev.zmeuion.vitalya.data.ValidatePassword
import dev.zmeuion.vitalya.data.ValidateUsername
import dev.zmeuion.vitalya.data.models.ValidationResult
import dev.zmeuion.vitalya.ui.screens.login.LoginEvent
import dev.zmeuion.vitalya.ui.screens.login.LoginUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository,
    private val validateUsername: ValidateUsername = ValidateUsername(),
    private val validateLogin: ValidateLogin = ValidateLogin(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UsernameChanged -> {
                _uiState.update { it.copy(username = event.username) }
            }

            is LoginEvent.LoginChanged -> {
                _uiState.update { it.copy(login = event.login) }
            }

            is LoginEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password) }
            }

            is LoginEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirm = event.repeatedPassword) }
            }

            is LoginEvent.ChangeAuthType -> {
                _uiState.update {
                    it.copy(
                        currentAuthType = !_uiState.value.currentAuthType,
                        username = "", login = "", password = "", confirm = ""
                    )
                }
            }

            is LoginEvent.Register -> {
                register()
            }

            is LoginEvent.Login -> {
                login()
            }

            is LoginEvent.DismissAuth -> {
                dismissAuth()
            }


        }

    }

    private fun matchPasswords(): ValidationResult {
        if (_uiState.value.password != _uiState.value.confirm) {
            return ValidationResult(successful = false, error = "Пароли не сходятся")
        }
        return ValidationResult(true)
    }

    private fun register() {

        val usernameResult = validateUsername.execute(_uiState.value.username)
        val loginResult = validateLogin.execute(_uiState.value.login)
        val passwordResult = validatePassword.execute(_uiState.value.password)
        val confirmResult = matchPasswords()
        _uiState.update { it.copy(isLoading = true) }

        val hasError = listOf(
            usernameResult,
            loginResult,
            passwordResult,
            confirmResult
        ).any { !it.successful }

        if (hasError) {
            _uiState.update {
                it.copy(
                    usernameError = usernameResult.error,
                    loginError = loginResult.error,
                    passwordError = passwordResult.error,
                    confirmError = confirmResult.error,
                    isLoading = false,
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                usernameError = null,
                loginError = null,
                passwordError = null,
                confirmError = null,
            )
        }

        viewModelScope.launch {
            try {
                repository.auth(
                    username = _uiState.value.username,
                    login = _uiState.value.login,
                    password = _uiState.value.password,
                    type = "register"
                )

                repository.updateUsername(repository.getToken())
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: NotFoundException) {
                _uiState.update { it.copy(loginError = "Логин уже занят") }
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(passwordError = "бекенд умер :(") }
                _uiState.update { it.copy(isLoading = false) }
            }
        }

    }

    private fun login() {
        val loginResult = validateLogin.execute(_uiState.value.login)
        val passwordResult = validatePassword.execute(_uiState.value.password)
        _uiState.update { it.copy(isLoading = true) }

        val hasError = listOf(
            loginResult,
            passwordResult,
        ).any { !it.successful }

        if (hasError) {
            _uiState.update {
                it.copy(
                    loginError = loginResult.error,
                    passwordError = passwordResult.error,
                    isLoading = false
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                usernameError = null,
                loginError = null,
                passwordError = null,
                confirmError = null,
            )
        }

        viewModelScope.launch {
            try {
                repository.auth(
                    username = _uiState.value.login,
                    login = _uiState.value.login,
                    password = _uiState.value.password,
                    type = "login"
                )
                repository.updateUsername(repository.getToken())
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: NotFoundException) {
                _uiState.update { it.copy(passwordError = "Неправильный пароль") }
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(passwordError = "бекенд умер :(") }
                _uiState.update { it.copy(isLoading = false) }
            }


        }

    }

    private fun dismissAuth() {
        viewModelScope.launch {
            repository.dismissAuth()
        }
    }


    sealed class ValidationEvent {
        data object Success : ValidationEvent()
    }

}