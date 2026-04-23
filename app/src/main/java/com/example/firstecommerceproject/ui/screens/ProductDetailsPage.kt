package com.example.firstecommerceproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.firstecommerceproject.domain.models.Product
import com.example.firstecommerceproject.ui.screens.components.PagerIndicator
import com.example.firstecommerceproject.ui.states.ProductDetailsUiState
import com.example.firstecommerceproject.ui.theme.FirstEcommerceProjectTheme
import com.example.firstecommerceproject.ui.viewmodel.ProductDetailsViewModel

/**
 * Stateful entry point for the Product Details screen.
 *
 * This composable connects the UI to the [ProductDetailsViewModel] and manages 
 * the initial data fetch side-effect.
 *
 * @param modifier Modifier for the container.
 * @param productId Unique identifier of the product to display.
 * @param viewModel ViewModel handling the state of this screen.
 * @param onBackClick Callback for navigating back to the previous screen.
 */
@Composable
fun ProductDetailsPage(
    modifier: Modifier = Modifier,
    productId: String,
    viewModel: ProductDetailsViewModel,
    onBackClick: () -> Unit
) {
    // Lifecycle-aware observation of UI state
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Trigger data fetch when the productId changes or on initial launch
    LaunchedEffect(productId) {
        viewModel.getProductDetails(productId)
    }

    ProductDetailsContent(
        modifier = modifier,
        uiState = uiState,
        onBackClick = onBackClick,
        onAddToCart = { /* TODO: Implement add to cart logic */ }
    )
}

/**
 * Pure stateless content for the Product Details screen.
 *
 * Better for testing and previews. Manages the high-level Scaffold layout including
 * the TopAppBar, BottomAppBar, and the loading/error/content states.
 *
 * @param uiState The current state of the product details.
 * @param modifier Modifier for the layout.
 * @param onBackClick Action to perform when back is pressed.
 * @param onAddToCart Action to perform when "Add to Cart" is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsContent(
    uiState: ProductDetailsUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAddToCart: (Product) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement wishlist toggle */ }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Add to Favorites"
                        )
                    }
                }
            )
        },
        bottomBar = {
            // Show bottom bar only when product data is successfully loaded
            uiState.product?.let { product ->
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Button(
                        onClick = { onAddToCart(product) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add to Cart")
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.product != null -> {
                    ProductDetailsBody(product = uiState.product)
                }
            }
        }
    }
}

/**
 * Scrollable body containing detailed information about the product.
 */
@Composable
private fun ProductDetailsBody(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Image Gallery
        ProductImageGallery(
            images = product.images,
            contentDescription = product.title
        )

        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            // Category Badge
            Text(
                text = product.category,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Main Title
            Text(
                text = product.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pricing Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Rs. ${product.offerPrice}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                if (product.originalPrice.isNotBlank() && product.originalPrice != product.offerPrice) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Rs. ${product.originalPrice}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            textDecoration = TextDecoration.LineThrough
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)

            Spacer(modifier = Modifier.height(24.dp))

            // Description Section
            Text(
                text = "Product Description",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 24.sp
            )

            // Extra padding to ensure content isn't hidden by the BottomAppBar
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

/**
 * A swipeable image gallery for the product details page.
 *
 * @param images List of image URLs to display.
 * @param contentDescription Description for accessibility.
 */
@Composable
private fun ProductImageGallery(
    images: List<String>,
    contentDescription: String
) {
    if (images.isEmpty()) {
        // Placeholder or empty state if no images are available
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { images.size })

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) { page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = "$contentDescription - Image ${page + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Optional: Overlay indicators or page numbers here if desired
        }

        if (images.size > 1) {
            Spacer(modifier = Modifier.height(8.dp))
            PagerIndicator(
                count = images.size,
                selectedIndex = pagerState.currentPage
            )
        }
    }
}

/**
 * Preview for the Product Details screen using a mock product.
 */
@Preview(showBackground = true)
@Composable
fun ProductDetailsPreview() {
    val mockProduct = Product(
        id = "1",
        title = "Premium Leather Watch",
        description = "This elegant leather watch is perfect for any occasion. It features a genuine leather strap and a stainless steel case with water resistance up to 50 meters.",
        offerPrice = "4500",
        originalPrice = "6000",
        category = "Accessories",
        images = listOf(
            "https://example.com/watch1.jpg",
            "https://example.com/watch2.jpg",
            "https://example.com/watch3.jpg"
        )
    )
    val mockUiState = ProductDetailsUiState(product = mockProduct)
    
    FirstEcommerceProjectTheme {
        ProductDetailsContent(uiState = mockUiState)
    }
}
