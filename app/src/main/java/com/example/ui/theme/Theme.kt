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
    primary = WarmOrangeDark,
    onPrimary = CharcoalTextPrimary,
    primaryContainer = WarmOrangeVariant,
    onPrimaryContainer = DarkTextPrimary,
    secondary = FreshHerbGreenDark,
    onSecondary = DarkBackground,
    secondaryContainer = Color(0xFF1B4D20),
    onSecondaryContainer = FreshHerbGreenDark,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val LightColorScheme = lightColorScheme(
    primary = WarmOrange,
    onPrimary = SurfaceWarm,
    primaryContainer = Color(0xFFFFCCBC),
    onPrimaryContainer = Color(0xFF4A1800),
    secondary = FreshHerbGreen,
    onSecondary = SurfaceWarm,
    secondaryContainer = FreshHerbGreenLight,
    onSecondaryContainer = Color(0xFF0F3813),
    background = CreamBackground,
    onBackground = CharcoalTextPrimary,
    surface = SurfaceWarm,
    onSurface = CharcoalTextPrimary,
    surfaceVariant = SurfaceWarmVariant,
    onSurfaceVariant = CharcoalTextSecondary,
    error = ChiliRed,
    onError = SurfaceWarm
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep warm food branding consistent
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
