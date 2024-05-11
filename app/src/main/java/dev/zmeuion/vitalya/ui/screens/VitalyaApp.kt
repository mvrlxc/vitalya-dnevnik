package dev.zmeuion.vitalya.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.BackoffPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dev.zmeuion.vitalya.R
import dev.zmeuion.vitalya.data.UpdateScheduleWorker
import dev.zmeuion.vitalya.database.DataStoreManager
import dev.zmeuion.vitalya.ui.screens.login.AuthScreen
import org.koin.androidx.compose.getViewModel
import java.time.Duration

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
    val context = LocalContext.current

    LaunchedEffect (Unit){
        WorkManager.getInstance(context).cancelAllWork()
        val work = PeriodicWorkRequestBuilder<UpdateScheduleWorker>(
            repeatInterval = Duration.ofSeconds(10)
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            duration = Duration.ofSeconds(30)
        )
            .build()
        WorkManager.getInstance(context).enqueue(work)
    }


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
                ScheduleScreen(
                    scheduleVM,
                    navToInfo = { navController.navigate("${Routes.LESSONINFO.text}/${it}") }
                )
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
                HomeWorkScreen(viewModel = scheduleVM)
            }
            composable(
                route = "${Routes.LESSONINFO.text}/{lessonId}", arguments = listOf(
                    navArgument("lessonId") { type = NavType.IntType })
            ) { backStackEntry ->
                LessonInfoScreen(
                    backStackEntry.arguments?.getInt("lessonId") ?: 0,
                    viewModel = scheduleVM,
                    onClick = { navController.navigateUp() })
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
    val items = listOf(
        NavBar(
            title = { Text(text = "Настройки") },
            route = Routes.OPTIONS,
            icon = Icons.Default.Settings,
        ),
        NavBar(
            title = { Text(text = "Расписание") },
            route = Routes.SCHEDULE,
            icon = Icons.Default.DateRange,
        ),
        NavBar(
            title = { Text(text = "Задания", maxLines = 1, overflow = TextOverflow.Ellipsis) },
            route = Routes.HOMEWORK,
            icon =  ImageVector.vectorResource(id = R.drawable.homework_svgrepo_com),
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
                icon = { Icon(item.icon, contentDescription = null, modifier = Modifier.size(22.dp)) },
                label = item.title
            )
        }
    }
}

data class NavBar(
    val title: @Composable () -> Unit,
    val route: Routes,
    val icon: ImageVector
)

enum class Routes(val text: String) {
    SCHEDULE(text = "Расписание"),
    OPTIONS(text = "Настройки"),
    HOMEWORK(text = "Домашние задания"),
    LESSONINFO(text = "Информация")
}

