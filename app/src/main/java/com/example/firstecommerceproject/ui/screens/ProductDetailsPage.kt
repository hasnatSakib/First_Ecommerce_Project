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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
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
import com.example.firstecommerceproject.domain.models.ProductVariant
import com.example.firstecommerceproject.ui.screens.components.PagerIndicator
import com.example.firstecommerceproject.ui.states.ProductDetailsUiState
import com.example.firstecommerceproject.ui.theme.FirstEcommerceProjectTheme
import com.example.firstecommerceproject.ui.viewmodel.ProductDetailsViewModel

/**
 * Stateful entry point for the Product Details screen.
 */
@Composable
fun ProductDetailsPage(
    modifier: Modifier = Modifier,
    productId: String,
    viewModel: ProductDetailsViewModel,
    onBackClick: () -> Unit
) {
    val productDetailsUiState by viewModel.productDetailsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(productId) {
        viewModel.getProductDetails(productId)
    }

    ProductDetailsContent(
        modifier = modifier,
        productDetailsUiState = productDetailsUiState,
        onBackClick = onBackClick,
        onAttributeSelected = viewModel::onAttributeSelected,
        onToggleWishlist = viewModel::onToggleWishlist,
        onAddToCart = { _, _ -> /* TODO */ }
    )
}

/**
 * Stateless content for the Product Details screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsContent(
    productDetailsUiState: ProductDetailsUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAttributeSelected: (String, String) -> Unit = { _, _ -> },
    onToggleWishlist: () -> Unit = {},
    onAddToCart: (Product, ProductVariant?) -> Unit = { _, _ -> }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onToggleWishlist) {
                        Icon(
                            imageVector = if (productDetailsUiState.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (productDetailsUiState.isFavourite) "Remove from Favorites" else "Add to Favorites",
                            tint = if (productDetailsUiState.isFavourite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        bottomBar = {
            productDetailsUiState.product?.let { product ->
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Button(
                        onClick = { onAddToCart(product, productDetailsUiState.selectedVariant) },
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
                productDetailsUiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                productDetailsUiState.errorMessage != null -> {
                    Text(
                        text = productDetailsUiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                productDetailsUiState.product != null -> {
                    ProductDetailsBody(
                        product = productDetailsUiState.product,
                        selectedVariant = productDetailsUiState.selectedVariant,
                        selectedAttributes = productDetailsUiState.selectedAttributes,
                        onAttributeSelected = onAttributeSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDetailsBody(
    product: Product,
    selectedVariant: ProductVariant?,
    selectedAttributes: Map<String, String>,
    onAttributeSelected: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Image Gallery: Show variant images if available, else product images
        val displayImages = if (selectedVariant != null && selectedVariant.variantImageUrls.isNotEmpty()) {
            selectedVariant.variantImageUrls
        } else if (product.imageUrls.isNotEmpty()) {
            product.imageUrls
        } else {
            listOf(product.thumbnailUrl)
        }

        ProductImageGallery(
            images = displayImages,
            contentDescription = product.name
        )

        Column(modifier = Modifier.padding(20.dp)) {
            // Brand & SKU
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.brand,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "SKU: ${selectedVariant?.sku ?: product.sku}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Name
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pricing
            val displayPrice = selectedVariant?.price ?: product.discountPrice
            val originalPrice = product.price

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Rs. $displayPrice",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                if (originalPrice > displayPrice) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Rs. $originalPrice",
                        style = MaterialTheme.typography.titleMedium.copy(
                            textDecoration = TextDecoration.LineThrough
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Attributes Selection
            if (product.attributes.isNotEmpty()) {
                AttributesSection(
                    attributes = product.attributes,
                    selectedAttributes = selectedAttributes,
                    onAttributeSelected = onAttributeSelected
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)

            Spacer(modifier = Modifier.height(24.dp))

            // Specifications
            if (product.specifications.isNotEmpty()) {
                SpecificationsSection(specifications = product.specifications)
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Description
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

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AttributesSection(
    attributes: Map<String, List<String>>,
    selectedAttributes: Map<String, String>,
    onAttributeSelected: (String, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        attributes.forEach { (name, values) ->
            Column {
                Text(
                    text = "Select ${name.replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    values.forEach { value ->
                        val isSelected = selectedAttributes[name] == value
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary 
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                                .clickable { onAttributeSelected(name, value) }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = value.replace("_", " ").replaceFirstChar { it.uppercase() },
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

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
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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

@Preview(showBackground = true)
@Composable
fun ProductDetailsPreview() {
    val mockProduct = Product(
        id = "1",
        name = "Sony WH-1000XM5 Wireless Headphones",
        brand = "Sony",
        sku = "SNY-XM5-BASE",
        description = "Industry-leading noise cancellation with two processors and 8 microphones.",
        price = 399.99,
        discountPrice = 348.0,
        category = listOf("electronics", "audio", "headphones"),
        thumbnailUrl = "https://example.com/sony_xm5.jpg",
        imageUrls = listOf("https://example.com/sony_xm5_1.jpg", "https://example.com/sony_xm5_2.jpg"),
        hasVariants = true,
        attributes = mapOf(
            "color" to listOf("navy_blue", "heather_gray"),
            "size" to listOf("s", "m", "l")
        ),
        specifications = mapOf(
            "Battery Life" to "30 Hours",
            "Bluetooth" to "Version 5.2"
        )
    )
    val mockUiState = ProductDetailsUiState(product = mockProduct)
    
    FirstEcommerceProjectTheme {
        ProductDetailsContent(productDetailsUiState = mockUiState)
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
                    .height(350.dp)
            ) { page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = "$contentDescription - Image ${page + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        if (images.size > 1) {
            Spacer(modifier = Modifier.height(12.dp))
            PagerIndicator(
                count = images.size,
                selectedIndex = pagerState.currentPage
            )
        }
    }
}
