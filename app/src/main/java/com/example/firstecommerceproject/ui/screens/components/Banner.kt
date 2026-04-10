package com.example.firstecommerceproject.ui.screens.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

private const val AUTO_SCROLL_DELAY = 5000L
private const val BANNER_ASPECT_RATIO = 2f

/**
 * A highly customizable auto-scrolling banner component for the home screen.
 * Features infinite circular scrolling, Material 3 styling, and responsive aspect ratio.
 *
 * @param bannerList List of image URLs to display in the banner.
 * @param modifier Modifier for the root container.
 * @param onBannerClick Callback invoked when a banner item is clicked.
 */
@Composable
fun BannerView(
    bannerList: List<String>,
    modifier: Modifier = Modifier,
    onBannerClick: (String) -> Unit = {}
) {
    if (bannerList.isEmpty()) return

    // Industry Standard: Use a large virtual count to simulate infinite scrolling.
    // This allows the user to swipe in both directions indefinitely.
    val virtualCount = if (bannerList.size > 1) Int.MAX_VALUE else bannerList.size

    // Calculate initial page to start in the middle of the virtual count, 
    // ensuring it aligns with the first item (index 0) of the actual list.
    val initialPage = if (bannerList.size > 1) {
        (virtualCount / 2) - (virtualCount / 2 % bannerList.size)
    } else 0

    val pagerState = rememberPagerState(
        pageCount = { virtualCount },
        initialPage = initialPage
    )

    // Side Effect: Auto-scroll logic.
    // It uses a while loop for continuous scrolling and checks 'isDragged' 
    // to pause whenever the user interacts with the pager.
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    LaunchedEffect(isDragged) {
        if (!isDragged && bannerList.size > 1) {
            while (true) {
                delay(AUTO_SCROLL_DELAY)
                // animateScrollToPage is a suspend function that ensures the 
                // animation completes before the next loop iteration.
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage + 1,
                    animationSpec = tween(durationMillis = 800)
                )
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                // Use aspect ratio instead of fixed height for better responsiveness across devices.
                .aspectRatio(BANNER_ASPECT_RATIO)
                .clip(MaterialTheme.shapes.medium),
            contentPadding = PaddingValues(horizontal = 0.dp),
            pageSpacing = 0.dp
        ) { page ->
            // Map the virtual page index back to the actual list index.
            val actualIndex = page % bannerList.size
            BannerItem(
                imageUrl = bannerList[actualIndex],
                onClick = { onBannerClick(bannerList[actualIndex]) }
            )
        }

        if (bannerList.size > 1) {
            Spacer(modifier = Modifier.height(12.dp))
            // Custom dot indicator with expansion animations.
            BannerIndicator(
                count = bannerList.size,
                selectedIndex = pagerState.currentPage % bannerList.size
            )
        }
    }
}

/**
 * Individual banner item card.
 */
@Composable
private fun BannerItem(
    imageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Promotion Banner",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

/**
 * A custom pager indicator with smooth color and width transitions.
 */
@Composable
private fun BannerIndicator(
    count: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(count) { iteration ->
            val isSelected = selectedIndex == iteration

            val color by animateColorAsState(
                targetValue = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                },
                label = "dotColor"
            )

            val width by animateDpAsState(
                targetValue = if (isSelected) 18.dp else 8.dp,
                label = "dotWidth"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(CircleShape)
                    .background(color)
                    .width(width)
                    .height(8.dp)
            )
        }
    }
}
