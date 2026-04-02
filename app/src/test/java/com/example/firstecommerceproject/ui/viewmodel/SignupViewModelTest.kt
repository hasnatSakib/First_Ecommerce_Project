package com.example.firstecommerceproject.ui.viewmodel

import app.cash.turbine.test
import com.example.firstecommerceproject.domain.use_case.AuthUseCases
import com.example.firstecommerceproject.domain.use_case.auth.GetCurrentUserUseCase
import com.example.firstecommerceproject.domain.use_case.auth.LoginUseCase
import com.example.firstecommerceproject.domain.use_case.auth.LogoutUseCase
import com.example.firstecommerceproject.domain.use_case.auth.SignupUseCase
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SignupViewModelTest {

    private lateinit var viewModel: SignupViewModel
    private lateinit var authUseCases: AuthUseCases
    private val loginUseCase: LoginUseCase = mockk()
    private val signupUseCase: SignupUseCase = mockk()
    private val logoutUseCase: LogoutUseCase = mockk()
    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authUseCases = AuthUseCases(
            login = loginUseCase,
            signup = signupUseCase,
            logout = logoutUseCase,
            getCurrentUser = getCurrentUserUseCase
        )
        viewModel = SignupViewModel(authUseCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onUsernameChange updates state`() = runTest {
        val newUsername = "testuser"
        viewModel.onUsernameChange(newUsername)
        assertEquals(newUsername, viewModel.signupUiState.value.username)
    }

    @Test
    fun `onEmailChange updates state`() = runTest {
        val newEmail = "test@example.com"
        viewModel.onEmailChange(newEmail)
        assertEquals(newEmail, viewModel.signupUiState.value.email)
    }

    @Test
    fun `onPasswordChange updates state`() = runTest {
        val newPassword = "password123"
        viewModel.onPasswordChange(newPassword)
        assertEquals(newPassword, viewModel.signupUiState.value.password)
    }

    @Test
    fun `onMobileChange updates state`() = runTest {
        val newMobile = "1234567890"
        viewModel.onMobileChange(newMobile)
        assertEquals(newMobile, viewModel.signupUiState.value.mobile)
    }

    @Test
    fun `onSignupClick success updates state correctly`() = runTest {
        val username = "testuser"
        val email = "test@example.com"
        val password = "password"
        val mockUser: FirebaseUser = mockk()

        coEvery { signupUseCase(username, email, password) } returns Result.success(mockUser)

        viewModel.onUsernameChange(username)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        viewModel.signupUiState.test {
            val initialState = awaitItem()
            assertFalse(initialState.isLoading)

            viewModel.onSignupClick()

            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertTrue(successState.isSignupSuccessful)
        }
    }

    @Test
    fun `onSignupClick failure updates state with error`() = runTest {
        val username = "testuser"
        val email = "test@example.com"
        val password = "password"
        val errorMessage = "Email already in use"

        coEvery { signupUseCase(username, email, password) } returns Result.failure(
            Exception(
                errorMessage
            )
        )

        viewModel.onUsernameChange(username)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        viewModel.signupUiState.test {
            awaitItem() // initial

            viewModel.onSignupClick()

            awaitItem() // loading

            val failureState = awaitItem()
            assertFalse(failureState.isLoading)
            assertEquals(errorMessage, failureState.errorMessage)
            assertFalse(failureState.isSignupSuccessful)
        }
    }
}
