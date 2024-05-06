package dev.zmeuion.vitalya

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import dev.zmeuion.vitalya.ui.screens.VitalyaApp
import dev.zmeuion.vitalya.ui.screens.login.RegisterScreen
import dev.zmeuion.vitalya.ui.theme.BobiTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            BobiTheme {
                VitalyaApp()
            }

        }
    }
}


