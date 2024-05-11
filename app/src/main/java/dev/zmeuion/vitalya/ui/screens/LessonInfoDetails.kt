package dev.zmeuion.vitalya.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieConstants
import dev.zmeuion.vitalya.database.ScheduleDBO
import dev.zmeuion.vitalya.network.models.CommentDTO

@Composable
fun LessonLoading(spec: LottieCompositionResult) {
    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
        LottieAnimation(composition = spec.value, iterations = LottieConstants.IterateForever)
    }
}

@Composable
fun NoHomework() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Icon(
            Icons.Filled.Clear,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = "Домашнее задание отсутсвует")
    }
}

@Composable
fun LessonTextFieldError(
    errorDescription: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.error.copy(
                    alpha = 0.7f
                )
            )
    ) {
        Text(
            text = errorDescription,
            color = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun LessonError(
    onRefreshClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.error.copy(
                    alpha = 0.7f
                )
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Проблемы с подключением:",
                modifier = Modifier.padding(end = 8.dp)
            )
            Button(onClick = onRefreshClick) {
                Text(text = "Повторить")
            }
        }
    }
}

@Composable
fun LessonNoComments(
    textFieldValue: String,
    isSending: Boolean,
    isError: Boolean,
    onSendClick: () -> Unit,
    onValueChange: (String) -> Unit,
    lottie2: LottieCompositionResult,
) {
    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            value = textFieldValue,
            isError = isError,
            onValueChange = onValueChange,
            label = { Text(text = "Введите ваше сообщение") },
            trailingIcon = {
                Box(
                ) {
                    if (!isSending) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable(onClick = onSendClick)
                        )
                    } else {
                        LottieAnimation(
                            composition = lottie2.value,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun HomeworkInfo(
    info: ScheduleDBO,
    isDiscussOpen: MutableState<Boolean>
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding()
        ) {
            Icon(
                Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Домашнее задание")
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface),
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
            Text(text = info.homework, modifier = Modifier.padding(8.dp))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding()
                .clickable { isDiscussOpen.value = !isDiscussOpen.value }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding()
            ) {
                Icon(
                    Icons.Filled.MailOutline,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Обсуждение")
            }
            Icon(
                if (isDiscussOpen.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun LessonInfoBasicInfo(
    info: ScheduleDBO
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = info.name,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = info.type,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
    Divider(thickness = 1.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
    ) {
        Icon(
            Icons.Default.Home,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = info.place)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding()
    ) {
        Icon(
            Icons.Filled.Person,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = info.teacher)
    }
    Divider(thickness = 1.dp, modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))
}

@Composable
fun LessonComments(
    commentsList: List<CommentDTO>,
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isSending: Boolean,
    composition: LottieCompositionResult,
    isError: Boolean,
    errorDescription: String,
) {
    Column {
        Column(
            modifier = Modifier
                .padding(top = 16.dp)
            //  .fillMaxHeight()

        ) {
            commentsList.forEach { items ->
                Comment(
                    username = items.username,
                    date = items.sendingDateTime,
                    content = items.content
                )
            }
        }
        Column {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                value = value,
                onValueChange = onValueChange,
                label = { Text(text = "Введите ваше сообщение") },
                isError = isError,
                trailingIcon = {
                    Box(
                    ) {
                        if (!isSending) {
                            Icon(
                                Icons.Default.Send,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable(onClick = onSendClick)
                            )
                        } else {
                            LottieAnimation(
                                composition = composition.value,
                                iterations = LottieConstants.IterateForever,
                                modifier = Modifier
                            )
                        }
                    }
                }
            )
        }
        if (isError) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.7f))
            ) {
                Text(text = errorDescription, color = MaterialTheme.colorScheme.background)
            }
        }
    }
}

@Composable
fun Comment(
    username: String,
    date: String,
    content: String,
) {
    Column(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inverseOnSurface)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                Icons.Rounded.AccountCircle,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = username, color = MaterialTheme.colorScheme.primary)
        }
        Text(text = content, Modifier.padding(4.dp))
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = date, fontSize = 12.sp)
        }
    }
}
