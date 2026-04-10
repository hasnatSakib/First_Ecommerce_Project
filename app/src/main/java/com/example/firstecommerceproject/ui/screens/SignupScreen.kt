package com.example.firstecommerceproject.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.firstecommerceproject.ui.screens.components.WaveHeader
import com.example.firstecommerceproject.ui.theme.FirstEcommerceProjectTheme
import com.example.firstecommerceproject.ui.util.AppUtil
import com.example.firstecommerceproject.ui.viewmodel.SignupViewModel

/**
 * Signup Screen for new users.
 * Handles user registration by collecting username, email, password, and mobile number.
 *
 * @param modifier Modifier for the screen container.
 * @param signupViewModel ViewModel managing signup state and actions.
 * @param onNavigateToLogin Navigation callback for users who already have an account.
 * @param onSignupSuccess Callback invoked upon successful registration.
 */
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    signupViewModel: SignupViewModel,
    onNavigateToLogin: () -> Unit,
    onSignupSuccess: () -> Unit
) {
    // Observe UI state from ViewModel using lifecycle-aware collector
    val signupUiState by signupViewModel.signupUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Handle navigation side-effect when registration is successful
    LaunchedEffect(signupUiState.isSignupSuccessful) {
        if (signupUiState.isSignupSuccessful) {
            AppUtil.showToast(context, "Signup Successful")
            onSignupSuccess()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
            )
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .align(Alignment.Center),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                // Visual header with wave effect and greeting
                WaveHeader(titleTop = "Hello,", titleMain = "Sign up!")

                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                ) {

                    // Username Input
                    OutlinedTextField(
                        value = signupUiState.username,
                        onValueChange = signupViewModel::onUsernameChange,
                        label = { Text("User name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(Modifier.height(16.dp))

                    // Email Input
                    OutlinedTextField(
                        value = signupUiState.email,
                        onValueChange = signupViewModel::onEmailChange,
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(Modifier.height(16.dp))

                    // Password Input with obfuscation
                    OutlinedTextField(
                        value = signupUiState.password,
                        onValueChange = signupViewModel::onPasswordChange,
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(Modifier.height(16.dp))

                    // Mobile Number Input
                    OutlinedTextField(
                        value = signupUiState.mobile,
                        onValueChange = signupViewModel::onMobileChange,
                        label = { Text("Mobile") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(Modifier.height(32.dp))

                    // Primary Signup Action Button
                    Button(
                        onClick = signupViewModel::onSignupClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !signupUiState.isLoading
                    ) {
                        Text(
                            text = if (signupUiState.isLoading) "SIGNING UP..." else "SIGN UP",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Navigation to Login Screen
                    TextButton(
                        onClick = onNavigateToLogin,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Already have an account? LOG IN",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Display error message if registration fails
                    signupUiState.errorMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Previews for SignupScreen in both light and dark modes.
 */
@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun PreviewSignupScreen() {
    FirstEcommerceProjectTheme {
        Surface {
            // Preview logic removed as it requires complex ViewModel mocking
        }
    }
}
