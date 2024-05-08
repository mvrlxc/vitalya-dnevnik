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
import dev.zmeuion.vitalya.network.models.CommentDTO
import dev.zmeuion.vitalya.ui.utils.formatDateFromMillis
import dev.zmeuion.vitalya.ui.utils.getCurrentDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
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

    fun getToken(): Flow<String> {

        return repository.getToken()
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

    fun getByID(id: Int) {
        viewModelScope.launch {
            val info = repository.getScheduleById(id)
            _uiState.update { it.copy(lessonInfo = info) }

        }

    }

    fun setDefault() {
        _uiState.update { it.copy(comments = listOf(noComments)) }
    }

    fun getComments(id: Int) {
        viewModelScope.launch {
            try {
                val coms = repository.getComments(id)
                if (coms.isEmpty()) {
                    _uiState.update { it.copy(comments = listOf(noComments), isLoading = false) }
                } else
                    _uiState.update { it.copy(comments = coms, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        comments = listOf(errorComments),
                        isLoading = false
                    )
                }
            }
        }

    }

    fun updateComment(input: String) {
        _uiState.update { it.copy(commentTF = input) }
    }

    fun postComment(lessonID: Int) {

        if (_uiState.value.commentTF.isBlank()) {
            _uiState.update { it.copy(isError = true, typeError = true) }
            return
        }
        if (_uiState.value.commentTF.length > 1000) {
            _uiState.update { it.copy(isError = true, typeError = false) }
            return
        }

        _uiState.update { it.copy(isSending = true, isError = false) }
        viewModelScope.launch {
            try {
                repository.postComment(
                    content = _uiState.value.commentTF,
                    lessonID = lessonID,
                    lessonDateTime = getCurrentDate()
                )
                val coms = repository.getComments(lessonID)

                _uiState.update {
                    it.copy(
                        isSending = false,
                        comments = coms,
                        isLoading = false,
                        commentTF = ""
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSending = false,
                        comments = listOf(errorComments)
                    )
                }
            }
        }

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

    val lessonInfo: ScheduleDBO = loading,

    val comments: List<CommentDTO> = emptyList(),
    val commentTF: String = "",
    val isSending: Boolean = false,
    val isLoading: Boolean = true,

    val isError: Boolean = false,
    val typeError: Boolean = false,

    val isNotRegister: Boolean = true,
)

val noComments = CommentDTO(lessonID = 0, username = "", content = "", sendingDateTime = "")
val errorComments = CommentDTO(lessonID = 0, username = "1", content = "1", sendingDateTime = "1")