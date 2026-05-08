package com.example.firstecommerceproject.domain.use_case.auth

import com.example.firstecommerceproject.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Use case for deleting the currently authenticated user's account.
 *
 * @property repository The repository to perform authentication operations.
 */
class DeleteAccountUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Executes the use case to delete the current user's account.
     * @return A [Result] containing true if successful, or an exception.
     */
    suspend operator fun invoke(): Result<Boolean> {
        return repository.deleteAccount()
    }
}
