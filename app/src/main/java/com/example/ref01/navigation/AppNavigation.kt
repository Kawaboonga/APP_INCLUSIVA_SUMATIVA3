package com.example.ref01.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ref01.navigation.routes.Screen
import com.example.ref01.ui.screens.*

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        // ---------- Auth ----------

        composable("scrollProbe") { ScrollProbe() }

        composable("scrollProbeM3") { ScrollProbeM3() }

        composable("probeBare") { ProbeBare() }





        composable(Screen.Login.route) {
            LoginScreen(
                onGoRegister = { navController.navigate(Screen.Register.route) },
                onGoForgot   = { navController.navigate(Screen.Forgot.route) },
                onLoginSuccess = {
                    // LOGIN -> FIRST, removiendo Login del backstack
                    navController.navigate(Screen.First.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegistered = { navController.popBackStack() } // vuelve a Login
            )
        }

        composable(Screen.Forgot.route) {
            ForgotPasswordScreen(onBack = { navController.popBackStack() })
        }

        // ---------- Flujo lineal ----------
        /**composable(Screen.First.route)  {
            FirstScreen(
                onNext = { navController.navigate(Screen.Second.route) },
                onBack = { /* Primera del flujo, no retrocede */ }
            )
        }**/

        composable(Screen.First.route)  {
            FirstScreen(
                navController = navController,                    // ✅ pásalo acá
                onNext = { navController.navigate(Screen.Second.route) },
                onBack = { /* Primera del flujo, no retrocede */ }
            )
        }

        composable(Screen.Second.route) {
            SecondScreen(
                onNext = { navController.navigate(Screen.Third.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Third.route)  {
            ThirdScreen(
                onNext = { navController.navigate(Screen.Fourth.route) },
                onBack = { navController.popBackStack() }
            )
        }

        // ✅ Fourth -> Home (sin limpiar pila)
        composable(Screen.Fourth.route) {
            FourthScreen(
                onFinish = {
                    navController.navigate(Screen.Home.route) {
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ---------- Libros ----------
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(
                navArgument(Screen.BookDetail.ARG_BOOK_ID) { type = NavType.IntType }
            )
        ) { backStack ->
            val id = backStack.arguments?.getInt(Screen.BookDetail.ARG_BOOK_ID) ?: -1
            BookDetailScreen(navController = navController, bookId = id)
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    AppNavigation(navController = navController, modifier = modifier)
}
