package dev.zmeuion.vitalya.ui.screens.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import org.koin.androidx.compose.getViewModel
import dev.zmeuion.vitalya.R

@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    errorText: String,
    icon: ImageVector,
    textRes: String,
    modifier: Modifier = Modifier,
    visualTransformation: Boolean = false,
) {
    val isVisible = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            leadingIcon = { Icon(icon, null) },
            placeholder = { Text(text = textRes) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            visualTransformation = if (!isVisible.value && visualTransformation) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (visualTransformation) Icon(
                    if (isVisible.value) painterResource(id = R.drawable.eye_3_svgrepo_com) else painterResource(
                        id = R.drawable.eye_slashed_svgrepo_com,

                        ),
                    contentDescription = null,
                    modifier
                        .size(40.dp)
                        .clickable(onClick = { isVisible.value = !isVisible.value })
                )
            }
        )
        if (isError) {
            Text(text = errorText, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun LoginButton(
    onClick: () -> Unit,
    text: String,
    isLoading: Boolean,
) {
    val lottie = rememberLottieComposition(spec = LottieCompositionSpec.Asset("loading.json"))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(25),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 24.dp, pressedElevation = 2.dp),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        if (!isLoading) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary

            )
        } else {
            LottieAnimation(composition = lottie.value, iterations = LottieConstants.IterateForever, modifier = Modifier.padding(16.dp))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    viewModel: LoginViewModel
) {
    val focusManager = LocalFocusManager.current
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is LoginViewModel.ValidationEvent.Success -> {}
            }
        }
    }
    Box(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = { focusManager.clearFocus() }
            )
        }
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {


            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Регистрация",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(24.dp)
                )

                LoginTextField(
                    value = uiState.value.username,
                    onValueChange = { viewModel.onEvent(LoginEvent.UsernameChanged(it)) },
                    isError = uiState.value.usernameError != null,
                    icon = Icons.Outlined.Face,
                    textRes = "Имя",
                    errorText = uiState.value.usernameError ?: "error",
                )
                LoginTextField(
                    value = uiState.value.login,
                    onValueChange = { viewModel.onEvent(LoginEvent.LoginChanged(it)) },
                    isError = uiState.value.loginError != null,
                    icon = Icons.Outlined.AccountCircle,
                    textRes = "Логин",
                    errorText = uiState.value.loginError ?: "error",
                )
                LoginTextField(
                    value = uiState.value.password,
                    onValueChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                    isError = uiState.value.passwordError != null,
                    icon = Icons.Outlined.Lock,
                    textRes = "Пароль",
                    errorText = uiState.value.passwordError ?: "error",
                    visualTransformation = true
                )
                LoginTextField(
                    value = uiState.value.confirm,
                    onValueChange = { viewModel.onEvent(LoginEvent.ConfirmPasswordChanged(it)) },
                    isError = uiState.value.confirmError != null,
                    icon = Icons.Outlined.CheckCircle,
                    textRes = "Подтверждение пароля",
                    errorText = uiState.value.confirmError ?: "error",
                    visualTransformation = true
                )

            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier)
                    LoginButton(
                        onClick = { viewModel.onEvent(LoginEvent.Register) },
                        text = "Зарегистрироваться",
                        isLoading = uiState.value.isLoading
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Уже есть аккаунт? ",
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Войти",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 24.sp,
                            modifier = Modifier.clickable { viewModel.onEvent(LoginEvent.ChangeAuthType) })
                    }
                    Text(text = "Продолжить без регистрации",
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .clickable { viewModel.onEvent(LoginEvent.DismissAuth) }
                            .padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel) {

    val uiState = viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = { focusManager.clearFocus() }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Вход",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(24.dp)
                )
                LoginTextField(
                    value = uiState.value.login,
                    onValueChange = { viewModel.onEvent(LoginEvent.LoginChanged(it)) },
                    isError = uiState.value.loginError != null,
                    icon = Icons.Outlined.AccountCircle,
                    textRes = "Логин",
                    errorText = uiState.value.loginError ?: "error",
                )
                LoginTextField(
                    value = uiState.value.password,
                    onValueChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                    isError = uiState.value.passwordError != null,
                    icon = Icons.Outlined.Lock,
                    textRes = "Пароль",
                    errorText = uiState.value.passwordError ?: "error",
                    visualTransformation = true
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.4f),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier)

                        LoginButton(
                            onClick = { viewModel.onEvent(LoginEvent.Login) },
                            text = "Войти",
                            isLoading = uiState.value.isLoading
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Ещё нет аккаунта? ",
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Создать",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 24.sp,
                                modifier = Modifier.clickable { viewModel.onEvent(LoginEvent.ChangeAuthType) })
                        }
                        Text(text = "Продолжить без регистрации",
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .clickable { viewModel.onEvent(LoginEvent.DismissAuth) }
                                .padding(top = 16.dp)
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun AuthScreen(
    viewModel: LoginViewModel = getViewModel<LoginViewModel>()
) {
    val currentAuthType = viewModel.uiState.collectAsState().value.currentAuthType
    if (currentAuthType) {
        RegisterScreen(viewModel = viewModel)
    } else {
        LoginScreen(viewModel = viewModel)

    }
}
