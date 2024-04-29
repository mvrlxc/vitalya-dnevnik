package dev.zmeuion.vitalya.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(

) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun updateTF(tf: String, value: String) {
        when (tf) {
            "username" -> _uiState.update { it.copy(usernameTFValue = value) }
            "login" -> _uiState.update { it.copy(loginTFValue = value) }
            "password" -> _uiState.update { it.copy(passwordTFValue = value) }
            "confirm" -> _uiState.update { it.copy(confirmTFValue = value) }
        }
    }

}

data class LoginUiState(
    val usernameTFValue: String = "",
    val loginTFValue: String = "",
    val passwordTFValue: String = "",
    val confirmTFValue: String = "",


)