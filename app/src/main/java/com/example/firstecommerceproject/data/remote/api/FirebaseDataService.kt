package com.example.firstecommerceproject.data.remote.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataService @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    val currentUser: FirebaseUser? get() = auth.currentUser

    suspend fun getBanners(): DocumentSnapshot? {
        return firestore.collection("data").document("banners").get().await()
    }

    suspend fun getCategories(): QuerySnapshot? {
        return firestore.collection("data").document("stock").collection("categories").get().await()
    }

    suspend fun getProducts(): QuerySnapshot? {
        return firestore.collection("data").document("stock").collection("products").get().await()
    }

    suspend fun getProductsByCategory(categoryName: String): QuerySnapshot? {
        return firestore.collection("data")
            .document("stock")
            .collection("products")
            .whereEqualTo("category", categoryName)
            .get()
            .await()
    }
}
