package dev.zmeuion.vitalya

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import dev.zmeuion.vitalya.ui.screens.OptionsViewModel
import dev.zmeuion.vitalya.ui.screens.VitalyaApp
import dev.zmeuion.vitalya.ui.screens.login.RegisterScreen
import dev.zmeuion.vitalya.ui.theme.BobiTheme
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            val optionsViewModel = getViewModel<OptionsViewModel>()
            val darkTheme = optionsViewModel.fetchTheme().collectAsState(initial = "system")
            val system = isSystemInDarkTheme()
            val currentTheme =
                when (darkTheme.value) {
                    "dark" -> true
                    "light" -> false
                    "system" -> system
                    else -> {
                        system
                    }
                }

            BobiTheme(darkTheme = currentTheme) {
                Box(modifier = Modifier.fillMaxSize()) {
                    VitalyaApp()
                }
            }
        }
    }
}


