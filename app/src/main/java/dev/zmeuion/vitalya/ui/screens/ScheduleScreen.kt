package dev.zmeuion.vitalya.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zmeuion.vitalya.database.ScheduleDBO
import org.koin.androidx.compose.getViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun LessonCard(
    name: String,
    type: String,
    place: String,
    timeStart: String,
    timeEnd: String,
    modifier: Modifier = Modifier,
    pairNumber: String,
) {
    val textColor = MaterialTheme.colorScheme.onBackground
    Column {


        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp))
                .fillMaxWidth(0.1f)
                .padding(top = 8.dp, bottom = 8.dp)
                .background(MaterialTheme.colorScheme.primary),
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
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(start = 8.dp, end = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
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
    schedule: List<ScheduleDBO>
) {
    LazyColumn {
        itemsIndexed(schedule) { _, item ->
            LessonCard(
                name = item.name,
                type = item.type,
                place = item.place,
                timeStart = item.timeStart,
                timeEnd = item.timeEnd,
                pairNumber = item.pairNumber,
            )
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleScreenViewModel = getViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()
    val datesList = uiState.value.datesRange
    val pagerState = rememberPagerState(
        initialPage = 0,
    ) {
        datesList.size
    }
    Column {


        Button(onClick = { viewModel.loadToDb() }) {

        }
        HorizontalPager(
            pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondBoundsPageCount = 2
        ) { page ->
            val bob = viewModel.getScheduleByDate(date = datesList[page])
                .collectAsState(initial = listOf())
            ScheduleBody(schedule = bob.value)

        }
    }
}

