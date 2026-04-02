package com.example.firstecommerceproject.domain.use_case

import com.example.firstecommerceproject.domain.use_case.auth.GetCurrentUserUseCase
import com.example.firstecommerceproject.domain.use_case.auth.LoginUseCase
import com.example.firstecommerceproject.domain.use_case.auth.LogoutUseCase
import com.example.firstecommerceproject.domain.use_case.auth.SignupUseCase

data class AuthUseCases(
    val login: LoginUseCase,
    val signup: SignupUseCase,
    val logout: LogoutUseCase,
    val getCurrentUser: GetCurrentUserUseCase
)
