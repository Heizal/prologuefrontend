package com.example.prologuefrontend.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = BackgroundLight,
    onBackground = TextPrimary,
    onSurface = TextSecondary
)

@Composable
fun PrologueTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}