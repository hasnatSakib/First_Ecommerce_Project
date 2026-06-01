package com.example.firstecommerceproject.domain.models

import com.google.firebase.firestore.DocumentId

/**
 * Data model representing a specific variant of a product in the sub-collection.
 */
data class ProductVariant(
    @DocumentId
    val id: String = "",
    val variantName: String = "",
    val sku: String = "",
    val price: Double = 0.0,
    val stockQuantity: Int = 0,
    val combination: Map<String, String> = emptyMap(),
    val imageUrl: String? = null,
    val variantImageUrls: List<String> = emptyList()
)
