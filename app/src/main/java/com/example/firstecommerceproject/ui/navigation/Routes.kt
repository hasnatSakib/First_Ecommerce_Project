package com.example.firstecommerceproject.ui.navigation

/**
 * Defines the navigation routes for the application.
 *
 * This sealed class ensures type-safety when navigating between screens using the
 * Compose Navigation component. Each data object/class represents a unique destination.
 *
 * @property route The string identifier used by the NavHost to identify the destination.
 */
sealed class Routes(
    val route: String
) {
    /** Route for the onboarding/authentication selection screen. */
    data object AuthScreen : Routes("authScreen")

    /** Route for the user login screen. */
    data object LoginScreen : Routes("loginScreen")

    /** Route for the user registration screen. */
    data object SignUpScreen : Routes("signupScreen")

    /** Route for the main dashboard/home screen after authentication. */
    data object LandingScreen : Routes("landingScreen")

    /** Route for displaying all product categories. */
    data object AllCategoriesScreen : Routes("allCategoriesScreen")

    /**
     * Route for displaying products belonging to a specific category.
     *
     * Uses dynamic path parameters to pass the category name/ID.
     */
    data object CategoryProductScreen : Routes("categoryProductScreen/{category}") {
        /**
         * Helper function to build the route string with a specific [category].
         */
        fun createRoute(category: String) = "categoryProductScreen/$category"
    }
}
