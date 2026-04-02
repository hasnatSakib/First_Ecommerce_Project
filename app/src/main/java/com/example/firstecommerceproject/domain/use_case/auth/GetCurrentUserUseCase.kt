package com.example.firstecommerceproject.domain.use_case.auth

import com.example.firstecommerceproject.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): FirebaseUser? {
        return repository.currentUser
    }
}