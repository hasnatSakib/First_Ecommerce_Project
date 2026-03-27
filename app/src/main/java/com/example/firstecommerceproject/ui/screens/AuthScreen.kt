package com.example.firstecommerceproject.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firstecommerceproject.R
import com.example.firstecommerceproject.ui.navigation.Routes
import com.example.firstecommerceproject.ui.theme.FirstEcommerceProjectTheme

@Composable
fun AuthScreen(modifier: Modifier, navController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner_1),
            contentDescription = "Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Start Shopping journey with DepotCart",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Shop Countrywide, Pick Up Locally",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )
        )
        Button(
            onClick = { navController.navigate(route = Routes.LoginScreen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(top = 20.dp),
            content = {
                Text(text = "Login", fontSize = 20.sp)
            }
        )
        OutlinedButton(
            onClick = { navController.navigate(route = Routes.SignUpScreen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(top = 20.dp),
            content = {
                Text(text = "Sign Up", fontSize = 20.sp)
            }
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun AuthScreenPreview() {
    FirstEcommerceProjectTheme {
        Surface {
            val navController = rememberNavController()
            AuthScreen(modifier = Modifier, navController = navController)
        }
    }
}
