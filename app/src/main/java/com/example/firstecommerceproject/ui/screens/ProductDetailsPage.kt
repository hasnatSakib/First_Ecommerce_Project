package com.example.firstecommerceproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
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
 * the initial data fetch side effect.
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
        onAddToCart = { _, _ -> 
            /* TODO: Implement add to cart logic */
        }
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
    onAddToCart: (Product, Map<String, String>) -> Unit = { _, _ -> }
) {
    // Selection state managed at the Content level for hoisting
    val selectedOptions = remember { mutableStateMapOf<String, String>() }

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
                val isOutOfStock = product.stockCount <= 0
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Button(
                        onClick = { onAddToCart(product, selectedOptions.toMap()) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isOutOfStock
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Shopping Cart")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isOutOfStock) "Out of Stock" else "Add to Cart")
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
                    ProductDetailsBody(
                        product = uiState.product,
                        selectedOptions = selectedOptions,
                        onOptionSelected = { key, value -> selectedOptions[key] = value }
                    )
                }
            }
        }
    }
}

/**
 * Scrollable body containing detailed information about the product.
 */
@Composable
private fun ProductDetailsBody(
    product: Product,
    selectedOptions: Map<String, String>,
    onOptionSelected: (String, String) -> Unit
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.category,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                StockStatusBadge(stockCount = product.stockCount)
            }

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

            // Dynamic Selectable Options (Size, Color, etc.)
            if (product.selectableOptions.isNotEmpty()) {
                SelectableOptionsSection(
                    options = product.selectableOptions,
                    selectedOptions = selectedOptions,
                    onOptionSelected = onOptionSelected
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)

            Spacer(modifier = Modifier.height(24.dp))

            // Technical Specifications Section
            if (product.specifications.isNotEmpty()) {
                SpecificationsSection(specifications = product.specifications)
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
                Spacer(modifier = Modifier.height(24.dp))
            }

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
 * A stunning section for selectable options like shoe size or shirt size.
 */
@Composable
private fun StockStatusBadge(stockCount: Int) {
    val (text, containerColor, contentColor) = when {
        stockCount <= 0 -> Triple("Out of Stock", MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer)
        stockCount < 5 -> Triple("Only $stockCount left!", MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)
        else -> Triple("In Stock", Color(0xFFE8F5E9), Color(0xFF2E7D32)) // Soft green for healthy stock
    }

    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = CircleShape,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

/**
 * A stunning section for selectable options like shoe size or shirt size.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectableOptionsSection(
    options: Map<String, List<String>>,
    selectedOptions: Map<String, String>,
    onOptionSelected: (String, String) -> Unit
) {
    Column {
        options.forEach { (optionName, values) ->
            Text(
                text = "Select $optionName",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                values.forEach { value ->
                    val isSelected = selectedOptions[optionName] == value
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary 
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary 
                                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onOptionSelected(optionName, value) }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = value,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary 
                                    else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * A clean, grid-like section for technical specifications.
 */
@Composable
private fun SpecificationsSection(specifications: Map<String, String>) {
    Column {
        Text(
            text = "Specifications",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                specifications.entries.forEachIndexed { index, entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = entry.key,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = entry.value,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (index < specifications.size - 1) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                            thickness = 1.dp
                        )
                    }
                }
            }
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
        ),
        selectableOptions = mapOf(
            "Size" to listOf("S", "M", "L", "XL"),
            "Color" to listOf("Black", "Brown", "Blue")
        ),
        specifications = mapOf(
            "Material" to "Genuine Leather",
            "Water Resistance" to "50m",
            "Warranty" to "2 Years"
        ),
        stockCount = 3
    )
    val mockUiState = ProductDetailsUiState(product = mockProduct)
    
    FirstEcommerceProjectTheme {
        ProductDetailsContent(uiState = mockUiState)
    }
}
