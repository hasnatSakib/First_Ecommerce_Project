package com.example.firstecommerceproject.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firstecommerceproject.ui.screens.AuthScreen
import com.example.firstecommerceproject.ui.screens.LoginScreen
import com.example.firstecommerceproject.ui.screens.SignupScreen
import com.example.firstecommerceproject.ui.viewmodel.LoginViewModel
import com.example.firstecommerceproject.ui.viewmodel.SignupViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier,loginViewModel: LoginViewModel,signupViewModel: SignupViewModel) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.AuthScreen.route) {
        composable(route =  Routes.AuthScreen.route) {
            AuthScreen(modifier, navController)
        }
        composable(route = Routes.LoginScreen.route) {
            LoginScreen(
                modifier = modifier,
                loginViewModel = loginViewModel,
                onNavigateToSignup = {
                    navController.navigate(Routes.SignUpScreen.route)
                }
            )
        }
        composable(route =  Routes.SignUpScreen.route) {
            SignupScreen(
                modifier = modifier,
                signupViewModel = signupViewModel,
                onNavigateToLogin = {
                    navController.navigate(Routes.LoginScreen.route)
                }
            )
        }
    }
}
