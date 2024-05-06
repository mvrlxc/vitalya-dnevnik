package dev.zmeuion.vitalya.ui.screens

import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zmeuion.vitalya.database.DataStoreManager
import dev.zmeuion.vitalya.ui.screens.login.LoginTextField
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.viewModel

val schedules = listOf("15.27Д-ПИ06/22б", "бобы", "ворлд оф варкрафт")

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OptionsScreen(
    viewModel: OptionsViewModel = getViewModel<OptionsViewModel>(),
) {
    val context = LocalContext.current
    val schedule = viewModel.getSchedule().collectAsState(initial = "")
    val username = viewModel.getUsername().collectAsState(initial = "2")
    val isAuth = remember {
        mutableStateOf(false)
    }
    val uiState = viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        isAuth.value = (username.value?.isNotBlank() ?: false && username.value != "2")
    }
    Scaffold(
        bottomBar = {
            if (isAuth.value) {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(MaterialTheme.colorScheme.error)
                        .clickable(onClick = { viewModel.exitAuth() }),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Выйти из аккаунта",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 24.sp
                    )
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (isAuth.value) {
                UserAccount(
                    username = username.value ?: "2",
                    text = "Вы вошли как: ",
                )
            } else {
                UserAccount(
                    username = "войти",
                    text = "Вы не вошли в аккаунт: ",
                    modifier = Modifier.clickable(onClick = { viewModel.exitAuth() })
                )
            }

            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Аккаунт")
                }
                OptionsCard(text = "Изменить имя", icon = Icons.Default.AccountBox, content = {
                    Column {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, end = 4.dp, bottom = 4.dp),
                            value = uiState.value.username,
                            onValueChange = { viewModel.updateUsernameTextField(it) },
                            isError = uiState.value.usernameError.isNotBlank(),
                            label = { Text(text = "Введите новое имя") },
                            trailingIcon = {
                                Box(
                                    modifier = Modifier
                                        .clickable(onClick = { viewModel.updateUsername() })
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
                                ) {
                                    Icon(
                                        Icons.Default.Done,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(28.dp)
                                    )
                                }
                            }
                        )
                        if (uiState.value.usernameError.isNotBlank()) {
                            Text(
                                text = uiState.value.usernameError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                })

                OptionsCard(text = "Изменить пароль", icon = Icons.Default.Lock) {

                }

                Box(
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Расписание")
                }

                OptionsCard(text = "Расписание по умолчанию", icon = Icons.Default.DateRange) {
                    SchedulePicker(schedule = schedule.value, onClick = {
                        viewModel.updateSchedule(it)
                    })
                }

                Button(onClick = { viewModel.start(context = context) }) {

                }


            }

        }
    }
}


@Composable
fun UserAccount(
    username: String,
    text: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp
            )
            Text(
                text = username,
                modifier = Modifier,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun OptionsCard(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    val isOpened = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .padding(top = 3.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .clickable(onClick = { isOpened.value = !isOpened.value })
    ) {
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    // modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier
                            .size(38.dp)
                            .padding(end = 4.dp)
                    )
                    Text(text = text)
                }
                if (isOpened.value) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                } else {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        if (isOpened.value) {
            content()
        }
    }
}

@Composable
fun SchedulePicker(
    onClick: (String) -> Unit,
    schedule: String
) {

    Box(
        modifier = Modifier
            .padding(top = 3.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inverseOnSurface),
        contentAlignment = Alignment.Center
    ) {
        Row {

            Text(text = "Выбранное расписание: ")
            Text(text = schedule, color = MaterialTheme.colorScheme.primary)
        }
    }
    LazyColumn {
        itemsIndexed(schedules) { _, schedule ->
            Box(
                modifier = Modifier
                    .padding(top = 3.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(onClick = { onClick(schedule) }),
                contentAlignment = Alignment.Center
            ) {
                Text(text = schedule)
            }
        }
    }
}
