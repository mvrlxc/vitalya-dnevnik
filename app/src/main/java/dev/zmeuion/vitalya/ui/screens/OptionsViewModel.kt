package dev.zmeuion.vitalya.ui.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dev.zmeuion.vitalya.data.OptionsRepository
import dev.zmeuion.vitalya.data.ScheduleRepository
import dev.zmeuion.vitalya.data.UpdateScheduleWorker
import dev.zmeuion.vitalya.data.ValidatePassword
import dev.zmeuion.vitalya.data.ValidateUsername
import dev.zmeuion.vitalya.database.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.concurrent.TimeUnit

class OptionsViewModel(
    private val repository: OptionsRepository,
    private val validateUsername: ValidateUsername = ValidateUsername(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(OptionsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getUsername()
        fetchTheme()
        getNotification()
    }

    fun getUsername(): Flow<String?> {
        return repository.getUsername()
    }

    fun exitAuth() {
        viewModelScope.launch {
            repository.deleteToken()
            repository.deleteUsername()
        }
    }

    private fun getNotification() {
        viewModelScope.launch {
            _uiState.update { it.copy(notifications = repository.getNoti()) }
        }
    }

    fun updateUsernameTextField(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updateNewPasswordTextField(username: String) {
        _uiState.update { it.copy(newPassword = username) }
    }

    fun updateOldPasswordTextField(username: String) {
        _uiState.update { it.copy(oldPassword = username) }
    }


    fun updateUsername() {
        val hasError = validateUsername.execute(_uiState.value.username)
        if (!hasError.successful) {
            _uiState.update { it.copy(usernameError = hasError.error ?: "") }
            return
        } else {
            try {
                viewModelScope.launch { repository.updateUsername(_uiState.value.username) }
                _uiState.update {
                    it.copy(
                        usernameError = "",
                        username = ""
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        usernameError = "Ошибка с подключением",
                    )
                }
            }
        }
    }

    fun updatePassword() {
        _uiState.update { it.copy(password2Error = "", passwordError = "") }
        val hasError = validatePassword.execute(_uiState.value.newPassword)
        var response = ""
        if (!hasError.successful) {
            _uiState.update { it.copy(passwordError = hasError.error ?: "") }
            return
        }
        try {
            viewModelScope.launch {
                response = repository.updatePassword(
                    _uiState.value.newPassword,
                    _uiState.value.oldPassword
                )

                if (response == "invalid") {
                    _uiState.update {
                        it.copy(
                            password2Error = "Неверный пароль",
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            usernameError = "",
                            newPassword = "",
                            oldPassword = "",
                        )
                    }
                }
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    passwordError = "Ошибка с подключением",
                )
            }
        }


    }

    fun updateSchedule(schedule: String) {
        viewModelScope.launch {
            repository.updateSchedule(schedule)
        }
    }

    fun getSchedule(): Flow<String> {
        return repository.getSchedule()
    }

    fun updateNotifications(input: Boolean) {
        viewModelScope.launch {
            repository.editNotifications(input)
        }
        _uiState.update { it.copy(notifications = input) }
    }

    fun chooseTheme(chosenTheme: String, type: Boolean) {
        viewModelScope.launch {
            when (chosenTheme) {
                "dark" -> {
                    if (type) {
                        repository.updateTheme("dark")
                        _uiState.update {
                            it.copy(
                                dark = true,
                                light = false,
                                system = false,
                            )
                        }
                    } else {
                        repository.updateTheme("system")
                        _uiState.update {
                            it.copy(
                                dark = false,
                                light = false,
                                system = true,
                            )
                        }

                    }
                }

                "light" -> {
                    if (type) {
                        repository.updateTheme("light")
                        _uiState.update {
                            it.copy(
                                dark = false,
                                light = true,
                                system = false,
                            )
                        }
                    } else {
                        repository.updateTheme("system")
                        _uiState.update {
                            it.copy(
                                dark = false,
                                light = false,
                                system = true,
                            )
                        }
                    }
                }

                "system" -> {
                    if (type) {
                        repository.updateTheme("system")
                        _uiState.update {
                            it.copy(
                                dark = false,
                                light = false,
                                system = true,
                            )
                        }
                    } else {
                        repository.updateTheme("dark")
                        _uiState.update {
                            it.copy(
                                dark = true,
                                light = false,
                                system = false,
                            )
                        }

                    }
                }
            }
        }
    }

    fun fetchTheme(): Flow<String> {
        return repository.getThemeFlow()
    }


}

data class OptionsUiState(
    val username: String = "",
    val usernameError: String = "",

    val newPassword: String = "",
    val oldPassword: String = "",
    val passwordError: String = "",
    val password2Error: String = "",

    val dark: Boolean = false,
    val light: Boolean = false,
    val system: Boolean = true,
    val theme: String = "system",

    val notifications: Boolean = false,
)
