package com.example.firstecommerceproject.domain.use_case.auth

import com.example.firstecommerceproject.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return repository.logout()
    }
}