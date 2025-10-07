package com.example.ref01.ui.theme

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

// -------------------- PALETA OSCURA --------------------
private val DarkColors: ColorScheme = darkColorScheme(
    primary = PurpleDark,
    onPrimary = Color.White,
    primaryContainer = PurpleDeep,
    onPrimaryContainer = Color.White,

    secondary = BlueStrong,
    onSecondary = Color.White,
    secondaryContainer = PurpleExtra,
    onSecondaryContainer = Color.White,

    tertiary = GreenAccent,
    onTertiary = Color.Black,

    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF121212),
    onSurface = Color.White
)

// -------------------- PALETA CLARA --------------------
private val LightColors: ColorScheme = lightColorScheme(
    primary = Purple,
    onPrimary = Color.White,
    primaryContainer = PurpleLight,
    onPrimaryContainer = Color.Black,

    secondary = BlueAccent,
    onSecondary = Color.White,
    secondaryContainer = PurpleMedium,
    onSecondaryContainer = Color.Black,

    tertiary = GreenAccent,
    onTertiary = Color.Black,

    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

// Tema base (sin animación)
@Composable
fun Ref01Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}

// Tema con transición suave (Crossfade)
@Composable
fun AnimatedRef01Theme(
    darkTheme: Boolean,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    // Calculamos el esquema destino según el modo
    val targetScheme: ColorScheme = when {
        dynamicColor -> if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme     -> DarkColors
        else          -> LightColors
    }

    // Suavizamos la transición con un fade en el fondo y superficies principales
    // (sin recrear el árbol completo). Si quieres algo más elaborado, lo ampliamos.
    val bg by animateColorAsState(targetScheme.background, label = "bg")
    val surface by animateColorAsState(targetScheme.surface, label = "surface")
    val onBg by animateColorAsState(targetScheme.onBackground, label = "onBg")
    val onSurface by animateColorAsState(targetScheme.onSurface, label = "onSurface")
    // El resto de colores los aplicamos directo (sin animar) para no sobrecargar.

    val animatedScheme = targetScheme.copy(
        background = bg,
        surface = surface,
        onBackground = onBg,
        onSurface = onSurface
    )

    MaterialTheme(
        colorScheme = animatedScheme,
        typography = Typography,
        content = content
    )
}

@Preview(name = "Tema Claro", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Tema Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun ThemePreview() {
    Ref01Theme { androidx.compose.material3.Text("Preview de tema") }
}
