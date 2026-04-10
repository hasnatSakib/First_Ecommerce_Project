package com.example.firstecommerceproject.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.firstecommerceproject.domain.models.Category
import com.example.firstecommerceproject.ui.screens.components.BannerView
import com.example.firstecommerceproject.ui.screens.components.CategoriesView
import com.example.firstecommerceproject.ui.screens.components.HeaderView
import com.example.firstecommerceproject.ui.states.HomeUiState
import com.example.firstecommerceproject.ui.theme.FirstEcommerceProjectTheme
import com.example.firstecommerceproject.ui.viewmodel.HomeViewModel

/**
 * Main Home Screen of the application.
 * Responsibilities include fetching initial data and coordinating the display of
 * banners, categories, and other home-related content.
 *
 * @param modifier Modifier for the screen container.
 * @param homeViewModel The ViewModel that manages the home screen's state.
 * @param onCategoryClick Callback when a category item is selected.
 * @param onSeeAllClick Callback when "See All" categories is clicked.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    onCategoryClick: (String) -> Unit,
    onSeeAllClick: () -> Unit
) {
    // Initial data fetch on first launch
    LaunchedEffect(Unit) {
        homeViewModel.getHomeData()
    }

    // Observe state from ViewModel using lifecycle-aware collector
    val homeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        modifier = modifier.fillMaxSize(),
        homeUiState = homeUiState,
        onBannerClick = { /* TODO: Handle specific banner click actions */ },
        onCategoryClick = onCategoryClick,
        onSeeAllClick = onSeeAllClick
    )
}

/**
 * Stateless content version of the Home Screen.
 * Better for previews and unit testing.
 *
 * @param homeUiState The current UI state containing banners, categories, etc.
 * @param modifier Modifier for the layout.
 * @param onBannerClick Action for banner interactions.
 * @param onCategoryClick Action for category interactions.
 * @param onSeeAllClick Action for "See All" categories interaction.
 */
@Composable
fun HomeScreenContent(
    homeUiState: HomeUiState,
    modifier: Modifier = Modifier,
    onBannerClick: (String) -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onSeeAllClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Welcoming header with username
        HeaderView(name = homeUiState.name ?: "User")

        Spacer(modifier = Modifier.height(24.dp))

        // Dynamic Promotional Banners
        if (homeUiState.banners.isNotEmpty()) {
            BannerView(
                modifier = Modifier.fillMaxWidth(),
                bannerList = homeUiState.banners,
                onBannerClick = onBannerClick
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Categories Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(onClick = onSeeAllClick) {
                Text(
                    text = "See All",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal list of categories
        CategoriesView(
            modifier = Modifier.fillMaxWidth(),
            categoriesList = homeUiState.categories,
            onCategoryClick = onCategoryClick
        )

        // Placeholder for future sections (Featured, New Arrivals, etc.)
        Spacer(modifier = Modifier.height(32.dp))
    }
}

/**
 * Previews for different themes and configurations.
 */
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun PreviewHomeScreen() {
    val mockUiState = HomeUiState(
        name = "Happy Customer",
        banners = listOf(
            "https://static.vecteezy.com/system/thumbnails/003/692/287/small/big-sale-discount-promotion-banner-template-with-blank-product-podium-scene-graphic-free-vector.jpg",
            "https://img.freepik.com/free-vector/black-friday-crazy-sale-banner-with-limited-time-offer_1017-41018.jpg?semt=ais_incoming&w=740&q=80"
        ),
        categories = listOf(
            Category(
                "1",
                "Electronics",
                "https://res.cloudinary.com/dupchkca1/image/upload/v1775304771/electronic-devices_qecjrk.png"
            ),
            Category("2", "Fashion", "https://example.com/fashion.png"),
            Category("3", "Home", "https://example.com/home.png"),
            Category("4", "Beauty", "https://example.com/beauty.png")
        )
    )
    FirstEcommerceProjectTheme {
        Surface {
            HomeScreenContent(homeUiState = mockUiState)
        }
    }
}
