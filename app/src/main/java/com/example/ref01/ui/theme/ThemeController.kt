package com.example.ref01.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

data class ThemeController(
    val isDark: Boolean,
    val toggle: () -> Unit
)

val LocalThemeController = staticCompositionLocalOf {
    ThemeController(isDark = false, toggle = {})
}
