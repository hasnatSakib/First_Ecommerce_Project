package com.example.firstecommerceproject.domain.models

import com.google.firebase.firestore.PropertyName

/**
 * Data model representing a product in the store.
 *
 * @property id Unique identifier for the product.
 * @property title The name of the product.
 * @property description Detailed description of the product.
 * @property originalPrice Price before any discounts.
 * @property offerPrice Price after applying discounts.
 * @property category The category name or ID the product belongs to.
 * @property images List of URLs for product images.
 * @property specifications Static technical details (e.g., "Material" -> "Cotton").
 * @property selectableOptions User-selectable variants (e.g., "Size" -> ["S", "M", "L"]).
 * @property stockCount Remaining inventory amount for this product.
 */
data class Product(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val originalPrice: String = "",
    val offerPrice: String = "",
    val category: String = "",
    val images: List<String> = emptyList(),
    val specifications: Map<String, String> = emptyMap(),
    val selectableOptions: Map<String, List<String>> = emptyMap(),
    @get:PropertyName("stockCounts")
    @set:PropertyName("stockCounts")
    var stockCount: Int = 0
)
