package com.example.firstecommerceproject.data.remote.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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
     * Fetches promotional banners from the "promotions/banners" document.
     *
     * @return A [DocumentSnapshot] containing banner data, or null if not found.
     */
    suspend fun getBanners(): DocumentSnapshot? {
        return firestore.collection("promotions").document("banners").get().await()
    }

    /**
     * Fetches all product categories from the "categories" collection.
     *
     * @return A [QuerySnapshot] containing all category documents.
     */
    suspend fun getCategories(): QuerySnapshot? {
        return firestore.collection("categories").get().await()
    }

    /**
     * Fetches all products from the "products" collection.
     *
     * @return A [QuerySnapshot] containing all product documents.
     */
    suspend fun getProducts(): QuerySnapshot? {
        return firestore.collection("products").get().await()
    }

    /**
     * Fetches products that belong to a specific category.
     *
     * @param categoryName The name of the category to filter by.
     * @return A [QuerySnapshot] containing products matching the category.
     */
    suspend fun getProductsByCategory(categoryName: String): QuerySnapshot? {
        return firestore.collection("products")
            .whereArrayContains("category", categoryName)
            .get()
            .await()
    }

    /**
     * Fetches a single product by its unique ID from the root "products" collection.
     *
     * @param productId The ID of the product to retrieve.
     * @return A [DocumentSnapshot] for the specified product ID.
     */
    suspend fun getProductById(productId: String): DocumentSnapshot? {
        return firestore.collection("products")
            .document(productId)
            .get()
            .await()
    }

    /**
     * Fetches all variants for a specific product.
     *
     * @param productId The ID of the parent product.
     * @return A [QuerySnapshot] containing variant documents.
     */
    suspend fun getProductVariants(productId: String): QuerySnapshot? {
        return firestore.collection("products")
            .document(productId)
            .collection("variants")
            .get()
            .await()
    }

    /**
     * Observes a single product by its unique ID in real-time.
     *
     * @param productId The ID of the product to observe.
     * @return A [Flow] of [DocumentSnapshot] updates.
     */
    fun observeProductById(productId: String): Flow<DocumentSnapshot?> = callbackFlow {
        val subscription = firestore.collection("products")
            .document(productId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot)
            }
        
        awaitClose { subscription.remove() }
    }

    /**
     * Toggles the wishlist status of a product for the current user.
     */
    suspend fun toggleWishlist(productId: String) {
        val uid = auth.currentUser?.uid ?: return
        val wishlistRef = firestore.collection("users").document(uid).collection("wishlist").document(productId)
        val snapshot = wishlistRef.get().await()
        
        if (snapshot.exists()) {
            wishlistRef.delete().await()
        } else {
            wishlistRef.set(mapOf("addedAt" to com.google.firebase.Timestamp.now())).await()
        }
    }

    /**
     * Checks if a product is in the user's wishlist.
     */
    suspend fun isProductInWishlist(productId: String): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        return firestore.collection("users").document(uid).collection("wishlist").document(productId).get().await().exists()
    }

    /**
     * Retrieves all product IDs in the user's wishlist.
     */
    suspend fun getWishlistProductIds(): List<String> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return firestore.collection("users").document(uid).collection("wishlist").get().await().documents.map { it.id }
    }

    /**
     * Toggles the favorite status of a product for the current user.
     */
    suspend fun toggleFavorite(productId: String) {
        val uid = auth.currentUser?.uid ?: return
        val favoriteRef = firestore.collection("users").document(uid).collection("favorites").document(productId)
        val snapshot = favoriteRef.get().await()
        
        if (snapshot.exists()) {
            favoriteRef.delete().await()
        } else {
            favoriteRef.set(mapOf("addedAt" to com.google.firebase.Timestamp.now())).await()
        }
    }

    /**
     * Checks if a product is in the user's favorites.
     */
    suspend fun isProductInFavorite(productId: String): Boolean {
        val uid = auth.currentUser?.uid ?: return false
        return firestore.collection("users").document(uid).collection("favorites").document(productId).get().await().exists()
    }

    /**
     * Retrieves all product IDs in the user's favorites.
     */
    suspend fun getFavoriteProductIds(): List<String> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return firestore.collection("users").document(uid).collection("favorites").get().await().documents.map { it.id }
    }
}
