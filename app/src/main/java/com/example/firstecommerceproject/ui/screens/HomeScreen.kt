package com.example.firstecommerceproject.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.firstecommerceproject.ui.screens.components.BannerView
import com.example.firstecommerceproject.ui.screens.components.HeaderView
import com.example.firstecommerceproject.ui.states.HomeUiState
import com.example.firstecommerceproject.ui.theme.FirstEcommerceProjectTheme
import com.example.firstecommerceproject.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel
) {
    LaunchedEffect(Unit) {
        homeViewModel.getHomeData()
    }
    val homeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        modifier = modifier,
        homeUiState = homeUiState,
        onBannerClick = { /* TODO: Handle click */ }
    )
}

@Composable
fun HomeScreenContent(
    homeUiState: HomeUiState,
    modifier: Modifier = Modifier,
    onBannerClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        HeaderView(name = homeUiState.name ?: "User")
        homeUiState.banners?.let {banners ->
            BannerView(
                modifier = Modifier.height(140.dp),
                bannerList = banners,
                onBannerClick = onBannerClick
            )
        }
    }
}


@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun PreviewHomeScreen() {
    val mockUiState = HomeUiState(
        name = "Happy Customer",
        banners = listOf(
            "https://static.vecteezy.com/system/resources/thumbnails/003/692/287/small/big-sale-discount-promotion-banner-template-with-blank-product-podium-scene-graphic-free-vector.jpg",
            "https://img.freepik.com/free-vector/black-friday-crazy-sale-banner-with-limited-time-offer_1017-41018.jpg?semt=ais_incoming&w=740&q=80"
        )
    )

    FirstEcommerceProjectTheme {
        Surface {
            HomeScreenContent(homeUiState = mockUiState)
        }
    }
}
