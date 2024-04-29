package dev.zmeuion.vitalya.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    icon: ImageVector,
    textRes: String,
    modifier: Modifier = Modifier
) {

    OutlinedTextField(
        modifier = modifier.padding(8.dp).fillMaxWidth(0.8f),
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        leadingIcon = { Icon(icon, null) },
        placeholder = { Text(text = textRes) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
    )

}

@Composable
fun RegisterScreen(
    viewModel: LoginViewModel = getViewModel<LoginViewModel>()
) {
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginTextField(
            value = uiState.value.usernameTFValue,
            onValueChange = { viewModel.updateTF("username", it) },
            isError = false,
            icon = Icons.Outlined.Face,
            textRes = "Имя"
        )
        LoginTextField(
            value = uiState.value.loginTFValue,
            onValueChange = { viewModel.updateTF("login", it) },
            isError = false,
            icon = Icons.Outlined.AccountCircle,
            textRes = "Логин"
        )
        LoginTextField(
            value = uiState.value.passwordTFValue,
            onValueChange = { viewModel.updateTF("password", it) },
            isError = false,
            icon = Icons.Outlined.Lock,
            textRes = "Пароль"
        )
        LoginTextField(
            value = uiState.value.confirmTFValue,
            onValueChange = { viewModel.updateTF("confirm", it) },
            isError = false,
            icon = Icons.Outlined.CheckCircle,
            textRes = "Подтверждение пароля"
        )


    }
}

@Composable
fun LoginScreen() {

}
