package com.example.firstecommerceproject.ui.viewmodel

import app.cash.turbine.test
import com.example.firstecommerceproject.domain.use_case.AuthUseCases
import com.example.firstecommerceproject.domain.use_case.auth.DeleteAccountUseCase
import com.example.firstecommerceproject.domain.use_case.auth.GetCurrentUserUseCase
import com.example.firstecommerceproject.domain.use_case.auth.GetNameUseCase
import com.example.firstecommerceproject.domain.use_case.auth.LoginUseCase
import com.example.firstecommerceproject.domain.use_case.auth.LogoutUseCase
import com.example.firstecommerceproject.domain.use_case.auth.SignupUseCase
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
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
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var authUseCases: AuthUseCases
    private val loginUseCase: LoginUseCase = mockk()
    private val signupUseCase: SignupUseCase = mockk()
    private val logoutUseCase: LogoutUseCase = mockk()
    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk()
    private val getNameUseCase: GetNameUseCase = mockk()
    private val deleteAccountUseCase: DeleteAccountUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authUseCases = AuthUseCases(
            login = loginUseCase,
            signup = signupUseCase,
            logout = logoutUseCase,
            getCurrentUser = getCurrentUserUseCase,
            getName = getNameUseCase,
            deleteAccount = deleteAccountUseCase
        )
        viewModel = LoginViewModel(authUseCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEmailChange updates state`() = runTest {
        val newEmail = "test@example.com"
        viewModel.onEmailChange(newEmail)
        assertEquals(newEmail, viewModel.loginUiState.value.email)
    }

    @Test
    fun `onPasswordChange updates state`() = runTest {
        val newPassword = "password123"
        viewModel.onPasswordChange(newPassword)
        assertEquals(newPassword, viewModel.loginUiState.value.password)
    }

    @Test
    fun `onLoginClick success updates state correctly`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val mockUser: FirebaseUser = mockk()

        coEvery { loginUseCase(email, password) } returns Result.success(mockUser)

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        viewModel.loginUiState.test {
            val initialState = awaitItem()
            assertFalse(initialState.isLoading)

            viewModel.onLoginClick()

            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertTrue(successState.isLoginSuccessful)
        }
    }

    @Test
    fun `onLoginClick failure updates state with error`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val errorMessage = "Invalid credentials"

        coEvery { loginUseCase(email, password) } returns Result.failure(Exception(errorMessage))

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        viewModel.loginUiState.test {
            awaitItem() // initial

            viewModel.onLoginClick()

            awaitItem() // loading

            val failureState = awaitItem()
            assertFalse(failureState.isLoading)
            assertEquals(errorMessage, failureState.errorMessage)
            assertFalse(failureState.isLoginSuccessful)
        }
    }

    @Test
    fun `onLogOutClick success updates state`() = runTest {
        coEvery { logoutUseCase() } returns Result.success(true)

        viewModel.loginUiState.test {
            awaitItem() // initial

            viewModel.onLogOutClick()

            val successState = awaitItem()
            assertTrue(successState.logoutSuccess)
            assertFalse(successState.isLoginSuccessful)
        }
    }

    @Test
    fun `resetLogoutState clears logout flag`() = runTest {
        coEvery { logoutUseCase() } returns Result.success(true)

        viewModel.onLogOutClick()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.loginUiState.value.logoutSuccess)

        viewModel.resetLogoutState()
        assertFalse(viewModel.loginUiState.value.logoutSuccess)
    }

    @Test
    fun `onEmailChange with invalid email sets emailError`() = runTest {
        val invalidEmail = "invalid-email"
        viewModel.onEmailChange(invalidEmail)
        assertEquals("Invalid email format", viewModel.loginUiState.value.emailError)
    }

    @Test
    fun `onEmailChange with valid email clears emailError`() = runTest {
        val validEmail = "test@example.com"
        viewModel.onEmailChange(validEmail)
        assertEquals(null, viewModel.loginUiState.value.emailError)
    }

    @Test
    fun `onPasswordChange with short password sets passwordError`() = runTest {
        val shortPassword = "123"
        viewModel.onPasswordChange(shortPassword)
        assertEquals("Password must be at least 6 characters", viewModel.loginUiState.value.passwordError)
    }

    @Test
    fun `onLoginClick with errors does not trigger loginUseCase`() = runTest {
        viewModel.onEmailChange("invalid")
        viewModel.onPasswordChange("123")
        
        viewModel.onLoginClick()
        
        // loginUiState.errorMessage should be set and isLoading should remain false
        assertTrue(viewModel.loginUiState.value.errorMessage != null)
        assertFalse(viewModel.loginUiState.value.isLoading)
    }

    @Test
    fun `isUserLoggedIn returns true when user exists`() {
        val mockUser: FirebaseUser = mockk()
        every { getCurrentUserUseCase() } returns mockUser

        assertTrue(viewModel.isUserLoggedIn())
    }

    @Test
    fun `isUserLoggedIn returns false when user is null`() {
        every { getCurrentUserUseCase() } returns null

        assertFalse(viewModel.isUserLoggedIn())
    }
}
