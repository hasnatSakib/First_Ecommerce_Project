package com.example.firstecommerceproject.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.firstecommerceproject.ui.screens.components.CategoryItem
import com.example.firstecommerceproject.ui.viewmodel.HomeViewModel

/**
 * Screen that displays all available product categories in a grid layout.
 *
 * This screen provides a comprehensive view of all categories, allowing users 
 * to easily browse and select a category to view its products.
 *
 * @param modifier Modifier for the screen container.
 * @param homeViewModel ViewModel used to retrieve the list of categories.
 * @param onCategoryClick Callback triggered when a category is selected.
 * @param onBackClick Callback to handle back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllCategoriesScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    onCategoryClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val homeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "All Categories") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(homeUiState.categories) { category ->
                CategoryItem(
                    category = category,
                    onCategoryClick = onCategoryClick
                )
            }
        }
    }
}
