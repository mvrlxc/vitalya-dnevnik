package dev.zmeuion.vitalya.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import org.koin.androidx.compose.getViewModel

@Composable
fun TestScreen(
    viewModel: TestScreenViewModel = getViewModel<TestScreenViewModel>()
) {
    val uiState = viewModel.uiState.collectAsState()
    Column {


        Text(text = uiState.value.text)
        Button(onClick = { viewModel.updateState() }) {
        }
        Button(onClick = {viewModel.loadToDb()}) {

        }
    }
}