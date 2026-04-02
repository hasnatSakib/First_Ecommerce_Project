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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.firstecommerceproject.ui.screens.components.WaveHeader
import com.example.firstecommerceproject.ui.theme.FirstEcommerceProjectTheme
import com.example.firstecommerceproject.ui.util.AppUtil
import com.example.firstecommerceproject.ui.viewmodel.SignupViewModel


@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    signupViewModel: SignupViewModel,
    onNavigateToLogin: () -> Unit,
    onSignupSuccess: () -> Unit
) {
    val signupUiState by signupViewModel.signupUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

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
                    listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0))
                )
            )
    ) {
        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .align(Alignment.Center),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column {

                WaveHeader("Hello,", "Sign up !")

                Column(modifier = Modifier.padding(20.dp)) {

                    OutlinedTextField(
                        value = signupUiState.username,
                        onValueChange = signupViewModel::onUsernameChange,
                        placeholder = { Text("User name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(14.dp))

                    OutlinedTextField(
                        value = signupUiState.email,
                        onValueChange = signupViewModel::onEmailChange,
                        placeholder = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(14.dp))

                    OutlinedTextField(
                        value = signupUiState.password,
                        onValueChange = signupViewModel::onPasswordChange,
                        placeholder = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(14.dp))

                    OutlinedTextField(
                        value = signupUiState.mobile,
                        onValueChange = signupViewModel::onMobileChange,
                        placeholder = { Text("Mobile") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = signupViewModel::onSignupClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6A11CB)
                        ),
                        enabled = !signupUiState.isLoading
                    ) {
                        Text(if (signupUiState.isLoading) "SIGNING UP..." else "SIGN UP")
                    }

                    Spacer(Modifier.height(16.dp))

                    TextButton(
                        onClick = onNavigateToLogin,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Already have an account? LOG IN")
                    }

                    signupUiState.errorMessage?.let {
                        Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
        }
    }
}


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
