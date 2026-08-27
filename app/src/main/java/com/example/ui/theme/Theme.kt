package com.example.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = OceanBluePrimary,
    onPrimary = Color.White,
    primaryContainer = OceanBlueDark,
    onPrimaryContainer = OceanBlueLight,
    secondary = SunsetOrangeSecondary,
    onSecondary = Color.White,
    secondaryContainer = SunsetOrangeDark,
    onSecondaryContainer = SunsetOrangeLight,
    tertiary = RoyalPurpleAdmin,
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    error = CoralRedAlert
)

private val LightColorScheme = lightColorScheme(
    primary = OceanBluePrimary,
    onPrimary = Color.White,
    primaryContainer = OceanBlueLight,
    onPrimaryContainer = OceanBlueDark,
    secondary = SunsetOrangeSecondary,
    onSecondary = Color.White,
    secondaryContainer = SunsetOrangeLight,
    onSecondaryContainer = SunsetOrangeDark,
    tertiary = RoyalPurpleAdmin,
    onTertiary = Color.White,
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = OceanBlueSurface,
    onSurfaceVariant = TextSecondaryDark,
    error = CoralRedAlert,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
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
