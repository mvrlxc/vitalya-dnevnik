package dev.zmeuion.vitalya.ui.screens

import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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

val schedules = listOf("15.27Д-ПИ06/22б", "15.228Д/ПИ1337", "69.52М/ИБ322")

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OptionsScreen(
    viewModel: OptionsViewModel = getViewModel(),
) {
    val context = LocalContext.current
    val schedule = viewModel.getSchedule().collectAsState(initial = "")
    val username = viewModel.getUsername().collectAsState(initial = "2")
    val darkTheme = viewModel.fetchTheme().collectAsState(initial = "system")


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
                        color = MaterialTheme.colorScheme.background,
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
                .verticalScroll(rememberScrollState())
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
                    Column {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, end = 4.dp, bottom = 4.dp),
                            value = uiState.value.oldPassword,
                            onValueChange = { viewModel.updateOldPasswordTextField(it) },
                            isError = uiState.value.password2Error.isNotBlank(),
                            label = { Text(text = "Введите старый пароль") },
                        )
                        if (uiState.value.password2Error.isNotBlank()) {
                            Text(
                                text = uiState.value.password2Error,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp, end = 4.dp, bottom = 4.dp),
                            value = uiState.value.newPassword,
                            onValueChange = { viewModel.updateNewPasswordTextField(it) },
                            isError = uiState.value.passwordError.isNotBlank(),
                            label = { Text(text = "Введите новый пароль") },
                            trailingIcon = {
                                Box(
                                    modifier = Modifier
                                        .clickable(onClick = { viewModel.updatePassword() })
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
                        if (uiState.value.passwordError.isNotBlank()) {
                            Text(
                                text = uiState.value.passwordError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
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
                Box(
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Приложение")
                }
                OptionsCard(text = "Тема", icon = Icons.Default.Build) {

                    Row {
                        ThemeSwitcher(
                            currentTheme = darkTheme.value,
                            onCheckDark = { viewModel.chooseTheme("dark", type = it) },
                            onCheckLight = { viewModel.chooseTheme("light", type = it) },
                            onCheckSystem = { viewModel.chooseTheme("system", type = it) }
                        )
                    }
                }

                OptionsCard(text = "Уведомления", icon = Icons.Default.Notifications) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Уведомления об изменениях в расписании")
                        Switch(
                            checked = uiState.value.notifications,
                            onCheckedChange = { viewModel.updateNotifications(it) })
                    }
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
    Column {
        schedules.forEach { schedule ->
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

@Composable
fun ThemeSwitcher(
    currentTheme: String,
    onCheckDark: (Boolean) -> Unit,
    onCheckLight: (Boolean) -> Unit,
    onCheckSystem: (Boolean) -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val darkSwitcher = remember {
            mutableStateOf(false)
        }
        val lightSwitcher = remember {
            mutableStateOf(false)
        }
        val systemSwitcher = remember {
            mutableStateOf(false)
        }
        when (currentTheme) {
            "dark" -> {
                darkSwitcher.value = true
                lightSwitcher.value = false
                systemSwitcher.value = false
            }

            "light" -> {
                darkSwitcher.value = false
                lightSwitcher.value = true
                systemSwitcher.value = false
            }

            "system" -> {
                darkSwitcher.value = false
                lightSwitcher.value = false
                systemSwitcher.value = true
            }
        }
        Text(text = "Тёмная")
        Switch(checked = darkSwitcher.value, onCheckedChange = { onCheckDark(it) })

        Text(text = "Светлая")
        Switch(checked = lightSwitcher.value, onCheckedChange = { onCheckLight(it) })

        Text(text = "Системная")
        Switch(checked = systemSwitcher.value, onCheckedChange = { onCheckSystem(it) })
    }

}
