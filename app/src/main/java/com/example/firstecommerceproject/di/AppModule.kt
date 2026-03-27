package com.example.firstecommerceproject.di

import com.example.firstecommerceproject.data.remote.api.FirebaseAuthService
import com.example.firstecommerceproject.data.repositoryImpl.AuthRepositoryImpl
import com.example.firstecommerceproject.domain.repository.AuthRepository
import com.example.firstecommerceproject.domain.use_case.AuthUseCases
import com.example.firstecommerceproject.domain.use_case.GetCurrentUserUseCase
import com.example.firstecommerceproject.domain.use_case.LoginUseCase
import com.example.firstecommerceproject.domain.use_case.LogoutUseCase
import com.example.firstecommerceproject.domain.use_case.SignupUseCase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: FirebaseAuthService
    ): AuthRepository = AuthRepositoryImpl(authService)

    @Provides
    @Singleton
    fun provideAuthUseCases(repository: AuthRepository): AuthUseCases {
        return AuthUseCases(
            login = LoginUseCase(repository),
            signup = SignupUseCase(repository),
            logout = LogoutUseCase(repository),
            getCurrentUser = GetCurrentUserUseCase(repository)
        )
    }
}
