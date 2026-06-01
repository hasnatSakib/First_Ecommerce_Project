package com.example.firstecommerceproject.domain.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

/**
 * Data model representing a product in the store according to the updated schema.
 */
data class Product(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val brand: String = "",
    val sku: String = "",
    val description: String = "",
    val thumbnailUrl: String = "",
    val imageUrls: List<String> = emptyList(),
    val price: Double = 0.0,
    val discountPrice: Double = 0.0,
    val hasVariants: Boolean = false,
    val createdAt: Timestamp? = null,
    val category: List<String> = emptyList(),
    val specifications: Map<String, String> = emptyMap(),
    val attributes: Map<String, List<String>> = emptyMap()
)
