package com.arda.campuslink.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Teal300,
    secondary = nightTextColor,
    background = bgColor,
    secondaryVariant = Purple800,
    error = errorColor
)

private val LightColorPalette = lightColors(
    primary = clickableColorLight,
    primaryVariant = Teal300,
    secondary = lightTextColor,
    background = bgColor2,
    secondaryVariant = textColor,
    error = errorColor
)

@Composable
fun CampusLinkTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = androidx.compose.material.Typography(),
        shapes = Shapes,
        content = content
    )
}