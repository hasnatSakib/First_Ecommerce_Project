package com.example.firstecommerceproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.firstecommerceproject.ui.navigation.AppNavigation
import com.example.firstecommerceproject.ui.theme.FirstEcommerceProjectTheme
import com.example.firstecommerceproject.ui.viewmodel.LoginViewModel
import com.example.firstecommerceproject.ui.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val signupViewModel : SignupViewModel by viewModels()
    private val loginViewModel : LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstEcommerceProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()){innerPadding ->
                    AppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        loginViewModel = loginViewModel,
                        signupViewModel = signupViewModel
                    )
                }
            }
        }
    }
}
