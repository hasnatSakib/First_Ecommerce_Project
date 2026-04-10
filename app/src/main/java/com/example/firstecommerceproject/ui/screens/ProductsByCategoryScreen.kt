package com.example.firstecommerceproject.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.firstecommerceproject.ui.viewmodel.ProductsByCategoryViewModel
import androidx.compose.material3.CircularProgressIndicator

/**
 * Screen displaying a list of products filtered by a specific category.
 *
 * This screen is navigated to from the Home screen when a category is selected.
 *
 * @param modifier Modifier for the screen container.
 * @param category The name or ID of the category to filter products by.
 * @param onBackClick Callback to handle the back navigation event.
 * @param productsByCategoryViewModel ViewModel for fetching products.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsByCategoryScreen(
    modifier: Modifier = Modifier,
    category: String,
    onBackClick: () -> Unit = {},
    productsByCategoryViewModel: ProductsByCategoryViewModel
) {
    val ProductsByCategoryUiState  by productsByCategoryViewModel.productsByCategoryUiState.collectAsStateWithLifecycle()


    LaunchedEffect(category) {
        productsByCategoryViewModel.getProductsByCategory(category)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = category) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (ProductsByCategoryUiState.isLoading) {
                CircularProgressIndicator()
            } else if (ProductsByCategoryUiState.errorMessage != null) {
                Text(text = ProductsByCategoryUiState.errorMessage!!)
            } else {
                ProductsByCategoryUiState.products.forEach { product->
                    Text(
                        text = "Found ${product.title} products for category: $category",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
