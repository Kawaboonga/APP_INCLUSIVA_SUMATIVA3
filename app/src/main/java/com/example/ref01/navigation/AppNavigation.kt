package com.example.ref01.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ref01.navigation.routes.Screen
import com.example.ref01.ui.screens.FirstScreen
import com.example.ref01.ui.screens.ForgotPasswordScreen
import com.example.ref01.ui.screens.FourthScreen
import com.example.ref01.ui.screens.LoginScreen
import com.example.ref01.ui.screens.RegisterScreen
import com.example.ref01.ui.screens.SecondScreen
import com.example.ref01.ui.screens.ThirdScreen



@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        composable(Screen.Login.route) {
            LoginScreen(
                onGoRegister = { navController.navigate(Screen.Register.route) },
                onGoForgot   = { navController.navigate(Screen.Forgot.route) },
                onLoginSuccess = {
                    //  a First y sacar login del backstack
                    navController.navigate(Screen.First.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
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

        //  flujo principal
        composable(Screen.First.route)  {
            FirstScreen(
                onNext = { navController.navigate(Screen.Second.route) },
                onBack = {  }
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
        composable(Screen.Fourth.route) {
            FourthScreen(
                onFinish = {

                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.First.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

