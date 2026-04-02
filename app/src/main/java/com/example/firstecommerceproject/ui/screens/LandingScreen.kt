package com.example.firstecommerceproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.firstecommerceproject.domain.models.NavItem
import com.example.firstecommerceproject.ui.viewmodel.HomeViewModel

@Composable
fun LandingScreen(
    modifier: Modifier,
    homeViewModel: HomeViewModel
) {
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Favorites", Icons.Default.Favorite),
        NavItem("Cart", Icons.Default.ShoppingCart),
        NavItem("Profile", Icons.Default.Person),
    )
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    Scaffold(
        modifier = modifier,
        topBar = {

        },
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = index == selectedIndex,
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        onClick = {selectedIndex = index }
                    )
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ContentScreen(
                    modifier = modifier,
                    homeViewModel = homeViewModel,
                    selectedIndex = selectedIndex
                )
            }
        }
    )
}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    selectedIndex: Int
) {
    when (selectedIndex) {
        0 -> HomeScreen(modifier, homeViewModel)
        1 -> FavoritesScreen(modifier)
        2 -> CartScreen(modifier)
        3 -> ProfileScreen(modifier)
    }
}

@Preview
@Composable
fun PreviewLandingScreen() {

}