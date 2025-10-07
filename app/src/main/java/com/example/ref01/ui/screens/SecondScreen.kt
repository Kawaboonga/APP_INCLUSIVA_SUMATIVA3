// ui/screens/SecondScreen.kt
package com.example.ref01.ui.screens

import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ref01.ui.components.ScreenScaffold
import com.example.ref01.ui.components.StepScreenLayout
import com.example.ref01.ui.theme.PurpleMedium
import com.example.ref01.ui.theme.Ref01Theme

@Composable
fun SecondScreen(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val titulo = "Segundo paso"
    val cuerpo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.."

    val paragraphProvider = remember(titulo, cuerpo) { { listOf(titulo, cuerpo) } }

    ScreenScaffold(
        title = titulo,
        canNavigateBack = true,
        onBack = onBack,
        //paragraphProvider = paragraphProvider,
        paragraphProvider = { emptyList() },
        showOverflowMenu = false,
        topBarColor = PurpleMedium,   // color de fondo del TopBar
        topBarIconSize = 28.dp        // íconos TTS más grandes
    ) {
        StepScreenLayout(
            title = titulo,
            body = cuerpo,
            onPrimary = onNext,
            onSecondary = onBack,
            primaryText = "Siguiente",
            secondaryText = "Volver",
            showSecondary = true       // en la 2/3/4 sí mostramos "Volver"
        )
    }
}

@Preview(name = "Second Claro", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Second Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewSecond() {
    Ref01Theme {
        // Para el preview, usamos el layout directamente.
        StepScreenLayout(
            title = "Segundo paso",
            body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            onPrimary = {},
            onSecondary = {}
        )
    }
}
