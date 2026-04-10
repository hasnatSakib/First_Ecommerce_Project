package com.example.firstecommerceproject.domain.models

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
 */
data class Product(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val originalPrice: String = "",
    val offerPrice: String = "",
    val category: String = "",
    val images: List<String> = emptyList(),
)
