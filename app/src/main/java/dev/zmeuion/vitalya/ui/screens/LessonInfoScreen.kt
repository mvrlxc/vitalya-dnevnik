package dev.zmeuion.vitalya.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.zmeuion.vitalya.database.ScheduleDBO
import dev.zmeuion.vitalya.network.models.CommentDTO

val bobi = listOf(true, false)

@OptIn(ExperimentalComposeUiApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LessonInfoBody(
    info: ScheduleDBO,
    modifier: Modifier = Modifier,
    commentsList: List<CommentDTO>,
    textFieldValue: String,
    onSendClick: () -> Unit,
    onValueChange: (String) -> Unit,
    isLoading: Boolean,
    isSending: Boolean,
    getComments: () -> Unit,
    isError: Boolean,
    errorDescription: String,
    token: State<String>

) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val isDiscussOpen = remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val lottie = rememberLottieComposition(spec = LottieCompositionSpec.Asset("loading.json"))
    val lottie2 = rememberLottieComposition(spec = LottieCompositionSpec.Asset("loading.json"))
    
    val isLogged = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(commentsList, onSendClick, onValueChange) {
        scrollState.scrollToItem(1)
    }


    LazyColumn(
        state = scrollState,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            },
    ) {
        itemsIndexed(bobi) { _, bobi ->

            if (bobi) {
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
                if (info.homework != "null") {
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

                        if (isDiscussOpen.value) {
                            if (token.value.length > 5) {
                                if (!isLoading) {
                                    if (commentsList != listOf(noComments)) {
                                        if (commentsList != listOf(errorComments)) {
                                            LessonComments(
                                                onSendClick = onSendClick,
                                                onValueChange = onValueChange,
                                                value = textFieldValue,
                                                commentsList = commentsList,
                                                composition = lottie2,
                                                isSending = isSending,
                                                isError = isError,
                                                errorDescription = errorDescription,
                                            )
                                        } else {
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
                                                    Button(onClick = getComments) {
                                                        Text(text = "Повторить")
                                                    }
                                                }
                                            }
                                        }
                                    } else {

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
                                        if (isError) {
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
                                    }
                                } else {
                                    Loading(spec = lottie)
                                }
                            } else
                            {
                                Text(text = "Авторизуйтесь, чтобы просматривать этот раздел")
                            }
                        }
                        
                    }
                } else {
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
            }
        }

    }
}


@Composable
fun LessonInfoTopBar(
    info: ScheduleDBO,
    onClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .height(IntrinsicSize.Max)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clickable(onClick = onClick)
        )
        Text(
            text = "${info.timeStart} - ${info.timeEnd}",
            modifier = Modifier.padding(start = 16.dp)
        )

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LessonInfoScreen(
    lessonId: Int,
    viewModel: ScheduleScreenViewModel,
    onClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.setDefault()
        viewModel.getByID(lessonId)
        viewModel.getComments(lessonId)
    }
    val uiState = viewModel.uiState.collectAsState()
    val token = viewModel.getToken().collectAsState(initial = "")

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = { LessonInfoTopBar(info = uiState.value.lessonInfo, onClick = onClick) }
    ) { innerPadding ->

        LessonInfoBody(
            info = uiState.value.lessonInfo,
            modifier = Modifier
                .imePadding()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding()
                ),
            commentsList = uiState.value.comments,
            onValueChange = { viewModel.updateComment(it) },
            onSendClick = { viewModel.postComment(lessonId) },
            textFieldValue = uiState.value.commentTF,
            isLoading = uiState.value.isLoading,
            isSending = uiState.value.isSending,
            getComments = { viewModel.getComments(lessonId) },
            isError = uiState.value.isError,
            errorDescription = if (uiState.value.typeError) "Поле не может быть пустым" else "Максимальная длина 1000 символов",
            token = token
        )

    }
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

@Composable
fun Loading(spec: LottieCompositionResult) {
    Box(modifier = Modifier, contentAlignment = Alignment.Center) {
        LottieAnimation(composition = spec.value, iterations = LottieConstants.IterateForever)
    }
}

