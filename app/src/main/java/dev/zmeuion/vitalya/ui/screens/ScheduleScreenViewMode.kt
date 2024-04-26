package dev.zmeuion.vitalya.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zmeuion.vitalya.data.ScheduleRepository
import dev.zmeuion.vitalya.database.ScheduleDBO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class ScheduleScreenViewModel(
    private val repository: ScheduleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ScheduleScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        datesRange()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun datesRange() {
        val startDate = LocalDate.of(2024, 4, 24)
        val endDate = LocalDate.of(2024, 5, 29)
        val datesList = mutableListOf<String>()
        var currentDate = startDate
        while (!currentDate.isAfter(endDate)) {
            datesList.add(currentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
            currentDate = currentDate.plusDays(1)
        }
        _uiState.update { it.copy(datesRange = datesList) }
    }

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

    fun getScheduleByDate(date: String): Flow<List<ScheduleDBO>> {
        return repository.getFromDbByDate(date)
    }
}

data class ScheduleScreenState(
    val datesRange: List<String> = listOf(),
    val text: String = ""
)