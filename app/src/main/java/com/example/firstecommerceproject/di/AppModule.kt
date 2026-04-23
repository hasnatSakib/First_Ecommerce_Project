package com.example.firstecommerceproject.di

import com.example.firstecommerceproject.data.remote.api.FirebaseAuthService
import com.example.firstecommerceproject.data.remote.api.FirebaseDataService
import com.example.firstecommerceproject.data.repositoryImpl.AuthRepositoryImpl
import com.example.firstecommerceproject.data.repositoryImpl.DataRepositoryImpl
import com.example.firstecommerceproject.domain.repository.AuthRepository
import com.example.firstecommerceproject.domain.repository.DataRepository
import com.example.firstecommerceproject.domain.use_case.AuthUseCases
import com.example.firstecommerceproject.domain.use_case.DataUseCases
import com.example.firstecommerceproject.domain.use_case.auth.GetCurrentUserUseCase
import com.example.firstecommerceproject.domain.use_case.auth.LoginUseCase
import com.example.firstecommerceproject.domain.use_case.auth.LogoutUseCase
import com.example.firstecommerceproject.domain.use_case.auth.SignupUseCase
import com.example.firstecommerceproject.domain.use_case.data.GetBannersUseCase
import com.example.firstecommerceproject.domain.use_case.data.GetCategoryUseCase
import com.example.firstecommerceproject.domain.use_case.auth.GetNameUseCase
import com.example.firstecommerceproject.domain.use_case.data.GetProductByIdUseCase
import com.example.firstecommerceproject.domain.use_case.data.GetProductsByCategoryUseCase
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
    fun provideDataRepository(
        dataService: FirebaseDataService,
    ): DataRepository = DataRepositoryImpl(dataService)

    @Provides
    @Singleton
    fun provideAuthUseCases(repository: AuthRepository): AuthUseCases {
        return AuthUseCases(
            login = LoginUseCase(repository),
            signup = SignupUseCase(repository),
            logout = LogoutUseCase(repository),
            getCurrentUser = GetCurrentUserUseCase(repository),
            getName = GetNameUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideDataUseCases(dataRepository: DataRepository): DataUseCases {
        return DataUseCases(
            getBanners = GetBannersUseCase(dataRepository),
            getCategories = GetCategoryUseCase(dataRepository),
            getProductsByCategory = GetProductsByCategoryUseCase(dataRepository),
            getProductById = GetProductByIdUseCase(dataRepository)
        )
    }
}
