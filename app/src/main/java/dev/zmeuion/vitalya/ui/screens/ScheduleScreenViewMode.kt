package dev.zmeuion.vitalya.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zmeuion.vitalya.data.ScheduleRepository
import dev.zmeuion.vitalya.database.ScheduleDBO
import dev.zmeuion.vitalya.ui.utils.formatDateFromMillis
import dev.zmeuion.vitalya.ui.utils.getCurrentDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
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
        pageInit()
        getGroup()
    }


    private fun getGroup() {
        viewModelScope.launch {
            repository.getGroup().collect { bob ->
                _uiState.update { it.copy(group = bob) }
            }
        }

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

    fun getScheduleByDate(date: String): Flow<List<ScheduleDBO>> {
        return repository.getFromDbByDate(date)
    }

    fun getScheduleByDateGroup(date: String): Flow<List<ScheduleDBO>> {
        return repository.getFromDbByDateGroup(date = date, group = _uiState.value.group)
    }


    fun pickDate() {
        _uiState.update { it.copy(isDatePickerOpen = true) }
    }

    fun dismissDatePicker() {
        _uiState.update { it.copy(isDatePickerOpen = false) }
    }

    private fun pageInit() {
        _uiState.update { it.copy(page = _uiState.value.datesRange.indexOf(getCurrentDate())) }
    }

    private fun updatePageState(page: Int) {
        _uiState.update { it.copy(page = page) }
    }

    @OptIn(ExperimentalFoundationApi::class)
    fun confirmDatePicker(
        dateMillis: Long?,
        pagerState: PagerState,
        dateList: List<String>,
        scope: CoroutineScope
    ) {
        val date = dateMillis?.let { formatDateFromMillis(it) } ?: getCurrentDate()
        scope.launch {
            pagerState.scrollToPage(dateList.indexOf(date))
        }
        _uiState.update { it.copy(isDatePickerOpen = false) }
    }
}

data class ScheduleScreenState(
    val datesRange: List<String> = listOf(),
    val isDatePickerOpen: Boolean = false,
    val text: String = "",
    val group: String = "",
    val isSchedulePicked: Boolean = true,
    val page: Int = 0,
)