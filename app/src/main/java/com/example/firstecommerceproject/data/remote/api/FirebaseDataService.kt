package com.example.firstecommerceproject.data.remote.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service class that interacts directly with Firebase services.
 *
 * This class provides methods to fetch banners, categories, and products from Firestore,
 * and manages authentication state via FirebaseAuth.
 *
 * @property auth The FirebaseAuth instance for user session management.
 * @property firestore The FirebaseFirestore instance for data retrieval.
 */
@Singleton
class FirebaseDataService @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    /**
     * Gets the currently authenticated user, or null if no user is signed in.
     */
    val currentUser: FirebaseUser? get() = auth.currentUser

    /**
     * Fetches promotional banners from the "data/banners" document.
     *
     * @return A [DocumentSnapshot] containing banner data, or null if not found.
     */
    suspend fun getBanners(): DocumentSnapshot? {
        return firestore.collection("data").document("banners").get().await()
    }

    /**
     * Fetches all product categories from the "data/stock/categories" collection.
     *
     * @return A [QuerySnapshot] containing all category documents.
     */
    suspend fun getCategories(): QuerySnapshot? {
        return firestore.collection("data").document("stock").collection("categories").get().await()
    }

    /**
     * Fetches all products from the "data/stock/products" collection.
     *
     * @return A [QuerySnapshot] containing all product documents.
     */
    suspend fun getProducts(): QuerySnapshot? {
        return firestore.collection("data").document("stock").collection("products").get().await()
    }

    /**
     * Fetches products that belong to a specific category.
     *
     * @param categoryName The name of the category to filter by.
     * @return A [QuerySnapshot] containing products matching the category.
     */
    suspend fun getProductsByCategory(categoryName: String): QuerySnapshot? {
        return firestore.collection("data")
            .document("stock")
            .collection("products")
            .whereEqualTo("category", categoryName)
            .get()
            .await()
    }

    /**
     * Fetches a single product by its unique ID.
     *
     * @param productId The ID of the product to retrieve.
     * @return A [DocumentSnapshot] for the specified product ID.
     */
    suspend fun getProductById(productId: String): DocumentSnapshot? {
        return firestore.collection("data")
            .document("stock")
            .collection("products")
            .document(productId)
            .get()
            .await()
    }
}
