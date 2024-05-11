package dev.zmeuion.vitalya.ui.screens

import android.annotation.SuppressLint
import android.inputmethodservice.Keyboard.Row
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDefaults.dateFormatter
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zmeuion.vitalya.database.ScheduleDBO
import dev.zmeuion.vitalya.ui.utils.DateView
import dev.zmeuion.vitalya.ui.utils.formatDate
import java.text.SimpleDateFormat
import java.util.Locale
import dev.zmeuion.vitalya.R
import dev.zmeuion.vitalya.ui.utils.getCurrentDate
import dev.zmeuion.vitalya.ui.utils.isDateValid
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date


//26

@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeWorkScreen(
    viewModel: ScheduleScreenViewModel
) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    val group = viewModel.uiState.collectAsState().value.group
    val scrollState = rememberScrollState()
    val data = viewModel.getAll().collectAsState(initial = listOf()).value.sortedBy {
        SimpleDateFormat(
            "dd.MM.yyyy",
            Locale.getDefault()
        ).parse(it.date)

    }
    val loadedDates = remember {
        mutableStateOf(listOf(""))
    }

    data.forEach {
        if (!loadedDates.value.contains(it.date) && (Date() <= dateFormat.parse(it.date) || getCurrentDate() == it.date)
        ) loadedDates.value =
            loadedDates.value.plus(it.date)
    }



    if (group.isBlank()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Выберите расписание в настройках")
        }
    } else {
        Column(

            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            loadedDates.value.forEach { item ->
                HomeworkBody(
                    viewModel = viewModel,
                    date = item
                )
            }
        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeworkBody(
    viewModel: ScheduleScreenViewModel,
    date: String
) {
    val data = viewModel.getScheduleByDateGroup(date).collectAsState(
        initial = listOf(
            loading
        )
    )


    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!data.value.all { it.homework.isBlank() || it.homework == "null" }) {
            Box(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.inverseOnSurface)

            ) {

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isDateValid(date)) formatDate(date).day.toString() else "",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 4.dp, start = 2.dp, top = 4.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (isDateValid(date)) stringResource(id = formatDate(date).month) else "",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 4.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isDateValid(date)) stringResource(id = formatDate(date).dayOfWeek) else "",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(4.dp)
                    )

                }
            }
        }
        data.value.forEach {
            if (it.homework != "null") {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 8.dp, end = 16.dp)
                ) {
                    Column(modifier = Modifier) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = it.name + ":",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = it.homework,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                    }
                }
            }
        }

    }
}
