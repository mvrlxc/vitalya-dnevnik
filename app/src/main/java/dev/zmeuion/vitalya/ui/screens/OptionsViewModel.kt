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

class OptionsViewModel(
    private val repository: OptionsRepository,
    private val validateUsername: ValidateUsername = ValidateUsername(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(OptionsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getUsername()
        fetchTheme()
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

    fun updateUsernameTextField(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updateUsername() {
        val hasError = validateUsername.execute(_uiState.value.username)
        if (!hasError.successful) {
            _uiState.update { it.copy(usernameError = hasError.error ?: "") }
            return
        } else {
            viewModelScope.launch { repository.updateUsername(_uiState.value.username) }
            _uiState.update {
                it.copy(
                    usernameError = "",
                    username = ""
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


    @RequiresApi(Build.VERSION_CODES.O)
    fun start(context: Context) {
        val work = PeriodicWorkRequestBuilder<UpdateScheduleWorker>(
            repeatInterval = Duration.ofMinutes(1)
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            duration = Duration.ofSeconds(30)
        )
            .build()
        WorkManager.getInstance(context).enqueue(work)
    }


}

data class OptionsUiState(
    val username: String = "",
    val usernameError: String = "",

    val dark: Boolean = false,
    val light: Boolean = false,
    val system: Boolean = true,
    val theme: String = "system"
)
