package com.example.firstecommerceproject.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstecommerceproject.domain.models.NavItem
import com.example.firstecommerceproject.ui.viewmodel.HomeViewModel

/**
 * Main application container screen that hosts the bottom navigation and top-level screens.
 *
 * This screen manages the selection of primary destinations like Home, Favorites,
 * Cart, and Profile. It uses a [Scaffold] to provide a standard Material 3 layout
 * structure with a bottom [NavigationBar].
 *
 * @param modifier Modifier for the landing screen container.
 * @param homeViewModel ViewModel providing data for the Home screen component.
 * @param onCategoryClick Callback triggered when a category is selected in the Home screen.
 * @param onSeeAllClick Callback when "See All" categories is clicked in the Home screen.
 */
@Composable
fun LandingScreen(
    modifier: Modifier,
    homeViewModel: HomeViewModel,
    onCategoryClick: (String) -> Unit,
    onSeeAllClick: () -> Unit
) {
    // List of top-level destinations for the bottom navigation bar
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Favorites", Icons.Default.Favorite),
        NavItem("Cart", Icons.Default.ShoppingCart),
        NavItem("Profile", Icons.Default.Person),
    )

    // Remembers the currently selected tab index across configuration changes
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                navItemList.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = index == selectedIndex,
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        onClick = { selectedIndex = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Renders the actual screen content based on the selected index
                ContentScreen(
                    modifier = Modifier.fillMaxSize(),
                    homeViewModel = homeViewModel,
                    selectedIndex = selectedIndex,
                    onCategoryClick = onCategoryClick,
                    onSeeAllClick = onSeeAllClick
                )
            }
        }
    )
}

/**
 * Helper composable that acts as a router for the [LandingScreen]'s main content area.
 *
 * @param modifier Modifier for the content container.
 * @param homeViewModel ViewModel shared with the [HomeScreen].
 * @param selectedIndex The index of the currently active bottom navigation item.
 * @param onCategoryClick Callback for category navigation events.
 * @param onSeeAllClick Callback for "See All" categories navigation.
 */
@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    selectedIndex: Int,
    onCategoryClick: (String) -> Unit,
    onSeeAllClick: () -> Unit
) {
    when (selectedIndex) {
        0 -> HomeScreen(
            modifier = modifier,
            homeViewModel = homeViewModel,
            onCategoryClick = onCategoryClick,
            onSeeAllClick = onSeeAllClick
        )
        1 -> FavoritesScreen(modifier)
        2 -> CartScreen(modifier)
        3 -> ProfileScreen(modifier)
    }
}

@Preview
@Composable
fun PreviewLandingScreen() {

}
