package dev.zmeuion.vitalya.ui.screens

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zmeuion.vitalya.data.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TestScreenViewModel(
    private val repository: ScheduleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TestState())
    val uiState = _uiState.asStateFlow()

    fun loadToDb() {
        viewModelScope.launch { repository.loadScheduleToDatabase() }

    }

    fun updateState() {

        viewModelScope.launch {
            repository.getFromDb().stateIn(
                scope = viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                initialValue = mutableListOf()
            ).collect { data ->
                _uiState.update { it.copy(text = data.toString()) }
            }
        }

    }


}

data class TestState(
    val text: String = ""
)