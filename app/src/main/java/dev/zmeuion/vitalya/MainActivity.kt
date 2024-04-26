package dev.zmeuion.vitalya

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import dev.zmeuion.vitalya.ui.screens.ScheduleScreen
import dev.zmeuion.vitalya.ui.theme.VitalyaTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VitalyaTheme {


                ScheduleScreen()
            }
        }
    }
}


