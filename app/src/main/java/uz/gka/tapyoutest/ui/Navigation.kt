package uz.gka.tapyoutest.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uz.gka.tapyoutest.presentation.main.MainScreen
import uz.gka.tapyoutest.presentation.result.ResultScreen

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Result : Screen("result")
}

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(onNavigate = { navController.navigate(Screen.Result.route) })
        }
        composable(Screen.Result.route) {
            ResultScreen()
        }
    }
}
