package dev.zmeuion.vitalya.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zmeuion.vitalya.database.ScheduleDBO
import org.koin.androidx.compose.getViewModel
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.snapshotFlow
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.zmeuion.vitalya.ui.utils.formatDate
import dev.zmeuion.vitalya.ui.utils.getCurrentDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

val loading = ScheduleDBO(
    0,
    "loading",
    "loading",
    "loading",
    "loading",
    "loading",
    "loading",
    "loading",
    "loading",
    0,
    "loading",
)

@Composable
fun LessonCard(
    name: String,
    type: String,
    place: String,
    timeStart: String,
    timeEnd: String,
    modifier: Modifier = Modifier,
    pairNumber: String,
    onClick: () -> Unit,
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    Column(
    ) {


        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp))
                .fillMaxWidth(0.1f)
                .padding(top = 8.dp, bottom = 8.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = pairNumber,
                modifier = Modifier.padding(2.dp),
                color = textColor,
                fontSize = 18.sp
            )
        }
        Card(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .fillMaxWidth()
                .clickable(onClick = onClick),
            //  .height(IntrinsicSize.Max),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier.fillMaxWidth(0.9f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            // .fillMaxHeight()
                            .padding(end = 16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = timeStart, color = textColor)
                        Text(text = timeEnd, color = textColor)
                    }
                    Column {
                        Text(
                            text = name,
                            color = textColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(text = type, color = textColor)
                        Text(text = place, color = textColor)
                    }
                }
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = textColor)
            }
        }
    }
}

@Composable
fun ScheduleBody(
    schedule: List<ScheduleDBO>,
    isGroupPicked: Boolean,
    modifier: Modifier,
    onClick: (Int?) -> Unit
) {
    if (schedule == listOf(loading)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {

        }
    } else if (schedule.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            if (!isGroupPicked) {
                Text(text = "Выберите расписание в настройках")
            } else {
                Text(text = "Расписание на сегодня отсутсвует")
            }
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            itemsIndexed(schedule) { _, item ->
                LessonCard(
                    name = item.name,
                    type = item.type,
                    place = item.place,
                    timeStart = item.timeStart,
                    timeEnd = item.timeEnd,
                    pairNumber = item.pairNumber.toString(),
                    onClick = { onClick(item.id) }
                )
            }
        }
    }
}

@Composable
fun ScheduleTopBar(
    date: String,
    onClick: () -> Unit
) {
    val dateData = formatDate(date)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = dateData.dayOfWeek),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
        )
        Row(
            modifier = Modifier.clickable(onClick = onClick)
        ) {
            Text(
                text = dateData.day.toString(),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = stringResource(id = dateData.month),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDatePicker(
    onConfirmClick: (Long?) -> Unit,
    onDismissClick: () -> Unit,
    openDialog: Boolean
) {


    if (openDialog) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = onDismissClick,
            confirmButton = {
                TextButton(
                    onClick = { onConfirmClick(datePickerState.selectedDateMillis) },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissClick
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }


}

@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleScreenViewModel,
    navToInfo: (Int?) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState()
    val datesList = uiState.value.datesRange
    val pagerState = rememberPagerState(
        initialPage = uiState.value.page,
    ) {
        datesList.size

    }


    ScheduleDatePicker(
        onConfirmClick = {
            viewModel.confirmDatePicker(
                it,
                scope = scope,
                pagerState = pagerState,
                dateList = datesList
            )
        },
        onDismissClick = { viewModel.dismissDatePicker() },
        openDialog = uiState.value.isDatePickerOpen
    )


    HorizontalPager(
        pagerState,
        modifier = Modifier
            .fillMaxSize(),

        beyondBoundsPageCount = 4
    ) { page ->

        val bob = viewModel.getScheduleByDateGroup(date = datesList[page])
            .collectAsState(initial = listOf(loading))
        Scaffold(
            topBar = { ScheduleTopBar(date = datesList[page], onClick = { viewModel.pickDate() }) }
        ) { innerPadding ->
            Column {
                ScheduleBody(
                    schedule = bob.value,
                    modifier = Modifier.padding(innerPadding),
                    isGroupPicked = uiState.value.group != "",
                    onClick = navToInfo
                )
            }
        }
    }
}
