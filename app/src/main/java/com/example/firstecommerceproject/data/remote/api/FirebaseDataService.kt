package com.example.firstecommerceproject.data.remote.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataService @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    val currentUser: FirebaseUser? get() = auth.currentUser

    suspend fun getName(): DocumentSnapshot? {

        val uid = auth.currentUser?.uid ?: return null
        return firestore.collection("users")
            .document(uid)
            .collection("info")
            .document("profile_data")
            .get()
            .await()
    }

    suspend fun getBanners(): DocumentSnapshot? {
        return firestore.collection("data").document("banners").get().await()
    }
}
