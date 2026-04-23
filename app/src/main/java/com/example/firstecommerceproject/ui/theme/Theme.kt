package com.example.firstecommerceproject.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * Default dark color scheme for the application.
 */
private val DarkColorScheme = darkColorScheme(
    primary = CoralPrimaryDark,
    onPrimary = Color(0xFF560000), // Darker red for text on coral
    primaryContainer = CoralPrimaryContainerDark,
    onPrimaryContainer = OnCoralPrimaryContainerDark,
    secondary = NavySecondaryDark,
    onSecondary = Color(0xFF000D60),
    secondaryContainer = NavySecondaryContainerDark,
    onSecondaryContainer = OnNavySecondaryContainerDark,
    tertiary = TealTertiaryDark,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = Color(0xFFE3E2E6),
    onSurface = Color(0xFFE3E2E6),
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark
)

private val LightColorScheme = lightColorScheme(
    primary = CoralPrimary,
    onPrimary = Color.White,
    primaryContainer = CoralPrimaryContainer,
    onPrimaryContainer = OnCoralPrimaryContainer,
    secondary = NavySecondary,
    onSecondary = Color.White,
    secondaryContainer = NavySecondaryContainer,
    onSecondaryContainer = OnNavySecondaryContainer,
    tertiary = TealTertiary,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight
)

/**
 * Main theme composable for the First Ecommerce Project.
 *
 * This theme supports Material 3 guidelines, including dynamic color on Android 12+ (API 31).
 * It automatically switches between light and dark color schemes based on system settings.
 *
 * @param darkTheme Whether to use the dark color scheme. Defaults to system setting.
 * @param dynamicColor Whether to use dynamic color from the system (Android 12+).
 * @param content The UI content to be themed.
 */
@Composable
fun FirstEcommerceProjectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}