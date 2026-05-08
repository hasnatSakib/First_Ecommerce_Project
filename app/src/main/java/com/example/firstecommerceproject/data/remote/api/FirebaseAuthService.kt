package com.example.firstecommerceproject.data.remote.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service responsible for authentication-related operations using Firebase Auth and Firestore.
 */
@Singleton
class FirebaseAuthService @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    /** The currently authenticated [FirebaseUser], or null if no user is signed in. */
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /**
     * Authenticates a user with the provided [email] and [password].
     */
    suspend fun signIn(email: String, password: String): FirebaseUser? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user
    }

    /**
     * Registers a new user with the provided [email] and [password].
     */
    suspend fun signUp(email: String, password: String): FirebaseUser? {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user
    }

    /**
     * Sends a verification email to the currently signed-in user.
     */
    suspend fun sendEmailVerification() {
        auth.currentUser?.sendEmailVerification()?.await()
    }

    /**
     * Saves additional user profile information to Firestore.
     */
    suspend fun saveUserData(uid: String, data: Map<String, Any>) {
        firestore.collection("users").document(uid).set(data).await()
    }

    /**
     * Signs out the current user.
     * @return true if successful, false otherwise.
     */
    fun signOut(): Boolean {
        return try {
            auth.signOut()
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Retrieves the current user's profile document from Firestore.
     */
    suspend fun getUserProfile(): DocumentSnapshot? {
        val uid = auth.currentUser?.uid ?: return null
        return firestore.collection("users")
            .document(uid)
            .get()
            .await()
    }

    /**
     * Retrieves the current user's name from Firestore.
     */
    suspend fun getName(): DocumentSnapshot? {
        return getUserProfile()
    }

    /**
     * Deletes the currently authenticated user's account and their profile data from Firestore.
     */
    suspend fun deleteAccount() {
        val uid = auth.currentUser?.uid
        auth.currentUser?.delete()?.await()
        if (uid != null) {
            firestore.collection("users").document(uid).delete().await()
        }
    }
}
