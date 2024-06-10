package relog.android.authentication.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import relog.android.authentication.ui.theme.auth.screens.LoginScreen
import relog.android.authentication.ui.theme.auth.screens.RegisterScreen
import relog.android.authentication.ui.theme.main.MainScreen

@Composable
fun AuthNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }

        composable("main") {
            MainScreen(navController = navController)
        }
    }
}