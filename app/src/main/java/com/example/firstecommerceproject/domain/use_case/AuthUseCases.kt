package com.example.firstecommerceproject.domain.use_case

data class AuthUseCases(
    val login: LoginUseCase,
    val signup: SignupUseCase,
    val logout: LogoutUseCase,
    val getCurrentUser: GetCurrentUserUseCase
)
