package com.example.firstecommerceproject.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.firstecommerceproject.ui.screens.AllCategoriesScreen
import com.example.firstecommerceproject.ui.screens.AuthScreen
import com.example.firstecommerceproject.ui.screens.ProductsByCategoryScreen
import com.example.firstecommerceproject.ui.screens.LandingScreen
import com.example.firstecommerceproject.ui.screens.LoginScreen
import com.example.firstecommerceproject.ui.screens.ProductDetailsPage
import com.example.firstecommerceproject.ui.screens.SignupScreen
import com.example.firstecommerceproject.ui.viewmodel.HomeViewModel
import com.example.firstecommerceproject.ui.viewmodel.LoginViewModel
import com.example.firstecommerceproject.ui.viewmodel.ProductDetailsViewModel
import com.example.firstecommerceproject.ui.viewmodel.ProductsByCategoryViewModel
import com.example.firstecommerceproject.ui.viewmodel.SignupViewModel
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Root navigation component that manages the app's screen transitions.
 *
 * This composable sets up the [NavHost] and defines all destinations within the application.
 * It handles the initial routing logic based on the user's authentication state.
 *
 * @param modifier Modifier for the NavHost container.
 * @param isLoggedIn Initial authentication state determining the start destination.
 * @param loginViewModel ViewModel for handling login logic and state.
 * @param signupViewModel ViewModel for handling registration logic and state.
 * @param homeViewModel ViewModel for the main dashboard/home content.
 */
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean,
    loginViewModel: LoginViewModel,
    signupViewModel: SignupViewModel,
    homeViewModel: HomeViewModel,
    productsByCategoryViewModel : ProductsByCategoryViewModel
) {
    val navController = rememberNavController()

    // Choose start destination based on whether user is already logged in
    val startDestination = if (isLoggedIn) Routes.LandingScreen.route else Routes.AuthScreen.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Authentication Selection Screen
        composable(route = Routes.AuthScreen.route) {
            AuthScreen(modifier, navController)
        }

        // Login Screen Flow
        composable(route = Routes.LoginScreen.route) {
            LoginScreen(
                modifier = modifier,
                loginViewModel = loginViewModel,
                onNavigateToSignup = {
                    navController.navigate(Routes.SignUpScreen.route)
                },
                onLoginSuccess = {
                    // Navigate to Landing and clear auth backstack to prevent going back to log in
                    navController.navigate(Routes.LandingScreen.route) {
                        popUpTo(Routes.AuthScreen.route) { inclusive = true }
                    }
                }
            )
        }

        // Signup Screen Flow
        composable(route = Routes.SignUpScreen.route) {
            SignupScreen(
                modifier = modifier,
                signupViewModel = signupViewModel,
                onNavigateToLogin = {
                    navController.navigate(Routes.LoginScreen.route)
                },
                onSignupSuccess = {
                    // Navigate to Landing and clear auth backstack
                    navController.navigate(Routes.LandingScreen.route) {
                        popUpTo(Routes.AuthScreen.route) { inclusive = true }
                    }
                }
            )
        }

        // Main App Dashboard
        composable(route = Routes.LandingScreen.route) {
            LandingScreen(
                modifier = modifier,
                homeViewModel = homeViewModel,
                onCategoryClick = { category ->
                    navController.navigate(Routes.CategoryProductScreen.createRoute(category))
                },
                onSeeAllClick = {
                    navController.navigate(Routes.AllCategoriesScreen.route)
                }
            )
        }

        // All Categories Screen
        composable(route = Routes.AllCategoriesScreen.route) {
            AllCategoriesScreen(
                modifier = modifier,
                homeViewModel = homeViewModel,
                onCategoryClick = { category ->
                    navController.navigate(Routes.CategoryProductScreen.createRoute(category))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Category-specific Product Listing
        composable(
            route = Routes.CategoryProductScreen.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            ProductsByCategoryScreen(
                modifier = modifier,
                category = category,
                onBackClick = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate(Routes.ProductDetailsScreen.createRoute(productId))
                },
                productsByCategoryViewModel = productsByCategoryViewModel
            )
        }

        // Product Details Screen
        composable(
            route = Routes.ProductDetailsScreen.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val productDetailsViewModel: ProductDetailsViewModel = hiltViewModel()
            ProductDetailsPage(
                modifier = modifier,
                productId = productId,
                viewModel = productDetailsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
