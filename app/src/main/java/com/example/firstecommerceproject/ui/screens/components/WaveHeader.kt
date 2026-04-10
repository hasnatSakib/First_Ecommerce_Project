package com.example.firstecommerceproject.ui.screens.components

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstecommerceproject.ui.theme.FirstEcommerceProjectTheme

/**
 * A decorative header component with a custom wave-shaped background.
 *
 * This component uses a [Canvas] to draw a cubic Bézier curve, creating a smooth 
 * wave effect at the bottom of the header. It supports a gradient background 
 * and displays two lines of text.
 *
 * @param titleTop Small introductory text displayed at the top (e.g., "Welcome to").
 * @param titleMain Large prominent text displayed below [titleTop] (e.g., the app name).
 */
@Composable
fun WaveHeader(titleTop: String, titleMain: String) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        // Drawing the custom wave background using a Path and linear gradient
        Canvas(modifier = Modifier.matchParentSize()) {
            val path = Path().apply {
                // Start below the top-left corner
                moveTo(0f, size.height * 0.7f)

                // Draw a cubic Bézier curve to create the wave shape
                // Control points are adjusted to create a balanced "S" curve
                cubicTo(
                    size.width * 0.25f, size.height,
                    size.width * 0.75f, size.height * 0.4f,
                    size.width, size.height * 0.7f
                )

                // Close the path by going to the top corners
                lineTo(size.width, 0f)
                lineTo(0f, 0f)
                close()
            }

            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    colors = listOf(primaryColor, secondaryColor)
                )
            )
        }

        // Header content positioned over the canvas
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = titleTop,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = titleMain,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun PreviewWaveHeader() {
    FirstEcommerceProjectTheme {
        Surface {
            WaveHeader("Welcome to", "E-Commerce App")
        }
    }
}
