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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.zmeuion.vitalya.data.UpdateScheduleWorker
import dev.zmeuion.vitalya.database.ScheduleDBO
import dev.zmeuion.vitalya.network.models.CommentDTO
import kotlinx.coroutines.delay
import java.time.Duration

val bobi = listOf(true, false)

@OptIn(ExperimentalMaterial3Api::class)
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

    val state = rememberPullToRefreshState()
    if (state.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.setDefault()
            viewModel.getByID(lessonId)
            viewModel.getComments(lessonId)
            delay(1500)
            state.endRefresh()
        }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = { LessonInfoTopBar(info = uiState.value.lessonInfo, onClick = onClick) }
    ) { innerPadding ->

        Box(modifier = Modifier
            .fillMaxSize()
            .nestedScroll(state.nestedScrollConnection)) {
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
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = state,
            )
        }
    }
}

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
    val scrollState = rememberLazyListState()
    val isDiscussOpen = remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current
    val lottie = rememberLottieComposition(spec = LottieCompositionSpec.Asset("loading.json"))
    val lottie2 = rememberLottieComposition(spec = LottieCompositionSpec.Asset("loading.json"))

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
                LessonInfoBasicInfo(info = info)
                if (info.homework != "null") {
                    HomeworkInfo(info = info, isDiscussOpen = isDiscussOpen)
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
                                        LessonError(onRefreshClick = getComments)
                                    }
                                } else {
                                    LessonNoComments(
                                        textFieldValue = textFieldValue,
                                        isSending = isSending,
                                        isError = isError,
                                        onSendClick = onSendClick,
                                        onValueChange = onValueChange,
                                        lottie2 = lottie2,
                                    )
                                    if (isError) {
                                        LessonTextFieldError(errorDescription = errorDescription)
                                    }
                                }
                            } else {
                                LessonLoading(spec = lottie)
                            }
                        } else {
                            Text(text = "Авторизуйтесь, чтобы просматривать этот раздел")
                        }
                    }
                } else {
                    NoHomework()
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






