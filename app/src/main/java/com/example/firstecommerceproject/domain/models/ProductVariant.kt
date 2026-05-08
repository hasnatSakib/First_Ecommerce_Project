package com.example.firstecommerceproject.domain.models

import com.google.firebase.firestore.DocumentId

/**
 * Data model representing a specific variant of a product in the sub-collection.
 */
data class ProductVariant(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val sku: String = "",
    val price: Double = 0.0,
    val discountPrice: Double = 0.0,
    val stockCount: Int = 0,
    val attributes: Map<String, String> = emptyMap()
)
