package dev.zmeuion.vitalya.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.zmeuion.vitalya.database.DataStoreManager
import dev.zmeuion.vitalya.ui.screens.login.AuthScreen
import org.koin.androidx.compose.getViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VitalyaApp() {
    val context = LocalContext.current
    val token = DataStoreManager(context).getTokenFlow().collectAsState(initial = "loading")
    if (token.value == "loading") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

        }
    } else if (token.value.isNotBlank()) {
        VitalyaMain()
    } else {
        AuthScreen()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VitalyaMain(

) {

    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val animValue = remember {
        mutableIntStateOf(3000)
    }
    val scheduleVM = getViewModel<ScheduleScreenViewModel>()

    Scaffold(
        bottomBar = {
            VitalyaBottomAppBar(
                navController = navController,
                backStack, animValue
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.SCHEDULE.text,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Routes.SCHEDULE.text, enterTransition =
            {
                slideInHorizontally(
                    initialOffsetX = { animValue.intValue },
                    animationSpec = tween(500)
                )
            }
            ) {
                ScheduleScreen(scheduleVM)
            }
            composable(route = Routes.OPTIONS.text, enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -3000 },
                    animationSpec = tween(500)
                )

            }) {
                OptionsScreen()
            }
            composable(route = Routes.HOMEWORK.text, enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 3000 },
                    animationSpec = tween(500)
                )


            }) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val lottieState = rememberLottieComposition(spec = LottieCompositionSpec.Asset("loading.json"))
                    LottieAnimation(composition = lottieState.value,  iterations = LottieConstants.IterateForever)
                }
            }
        }
    }

}

@Composable
fun VitalyaBottomAppBar(
    navController: NavController,
    backStack: NavBackStackEntry?,
    animValue: MutableIntState,
) {
    var selectedItem by remember { mutableIntStateOf(1) }
    val items = listOf<NavBar>(
        NavBar(
            title = "Настройки",
            route = Routes.OPTIONS,
            icon = Icons.Default.Settings,
        ),
        NavBar(
            title = "Расписание",
            route = Routes.SCHEDULE,
            icon = Icons.Default.DateRange,
        ),
        NavBar(
            title = "Домашние задания",
            route = Routes.HOMEWORK,
            icon = Icons.Default.ShoppingCart,
        )
    )

    NavigationBar(
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    if ((backStack?.destination?.route ?: "") == Routes.OPTIONS.text) {
                        animValue.intValue = 3000
                    } else
                        animValue.intValue = -3000
                    navController.navigate(item.route.text) {
                        navController.popBackStack(inclusive = true, route = item.route.text)
                    }
                },
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(text = item.title) }
            )
        }
    }
}

data class NavBar(
    val title: String,
    val route: Routes,
    val icon: ImageVector
)

enum class Routes(val text: String) {
    SCHEDULE(text = "Расписание"),
    OPTIONS(text = "Настройки"),
    HOMEWORK(text = "Домашние задания"),
}

