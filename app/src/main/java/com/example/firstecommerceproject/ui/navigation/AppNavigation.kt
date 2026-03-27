package com.example.firstecommerceproject.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firstecommerceproject.ui.screens.AuthScreen
import com.example.firstecommerceproject.ui.screens.HomeScreen
import com.example.firstecommerceproject.ui.screens.LoginScreen
import com.example.firstecommerceproject.ui.screens.SignupScreen
import com.example.firstecommerceproject.ui.viewmodel.LoginViewModel
import com.example.firstecommerceproject.ui.viewmodel.SignupViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean,
    loginViewModel: LoginViewModel, signupViewModel: SignupViewModel,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.HomeScreen.route else Routes.AuthScreen.route
    ) {
        composable(route = Routes.AuthScreen.route) {
            AuthScreen(modifier, navController)
        }
        composable(route = Routes.LoginScreen.route) {
            LoginScreen(
                modifier = modifier,
                loginViewModel = loginViewModel,
                onNavigateToSignup = {
                    navController.navigate(Routes.SignUpScreen.route)
                },
                onLoginSuccess = {
                    navController.navigate(Routes.HomeScreen.route)
                }
            )
        }
        composable(route = Routes.SignUpScreen.route) {
            SignupScreen(
                modifier = modifier,
                signupViewModel = signupViewModel,
                onNavigateToLogin = {
                    navController.navigate(Routes.LoginScreen.route)
                },
                onSignupSuccess = {
                    navController.navigate(Routes.HomeScreen.route)
                }
            )
        }
        composable(route = Routes.HomeScreen.route) {
            HomeScreen(modifier = modifier)
        }
    }
}
