package dev.zmeuion.vitalya

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.zmeuion.vitalya.ui.screens.LoginScreen
import dev.zmeuion.vitalya.ui.screens.RegisterScreen
import dev.zmeuion.vitalya.ui.screens.ScheduleScreen
import dev.zmeuion.vitalya.ui.theme.BobiTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            BobiTheme {
                RegisterScreen()
            }

        }
    }
}


