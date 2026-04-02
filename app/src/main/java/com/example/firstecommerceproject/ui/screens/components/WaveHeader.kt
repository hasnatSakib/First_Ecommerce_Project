package com.example.firstecommerceproject.ui.screens.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.firstecommerceproject.ui.theme.FirstEcommerceProjectTheme

// ---------- WAVE HEADER ----------
@Composable
fun WaveHeader(titleTop: String, titleMain: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {

        Canvas(modifier = Modifier.matchParentSize()) {
            val path = Path().apply {
                moveTo(0f, size.height * 0.7f)
                cubicTo(
                    size.width * 0.25f, size.height,
                    size.width * 0.75f, size.height * 0.4f,
                    size.width, size.height * 0.7f
                )
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }

            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2))
                )
            )
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(titleTop, color = Color.White, fontSize = 14.sp)
            Text(
                titleMain,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun PreviewSignupScreen() {
    FirstEcommerceProjectTheme {
        Surface {
            WaveHeader("Welcome to", "E-Commerce App")
        }
    }
}