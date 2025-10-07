// ui/screens/ThirdScreen.kt
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
fun ThirdScreen(
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val titulo = "Tercer paso"
    val cuerpo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.."

    val paragraphProvider = remember(titulo, cuerpo) { { listOf(titulo, cuerpo) } }

    ScreenScaffold(
        title = titulo,
        canNavigateBack = true,
        onBack = onBack,
        //paragraphProvider = paragraphProvider,
        paragraphProvider = { emptyList() },
        showOverflowMenu = false,
        topBarColor = PurpleMedium,
        topBarIconSize = 28.dp
    ) {
        StepScreenLayout(
            title = titulo,
            body = cuerpo,
            onPrimary = onNext,
            onSecondary = onBack,
            primaryText = "Siguiente",
            secondaryText = "Volver",
            showSecondary = true
        )
    }
}

@Preview(name = "Third Claro", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Third Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewThird() {
    Ref01Theme {
        StepScreenLayout(
            title = "Tercer paso",
            body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            onPrimary = {},
            onSecondary = {}
        )
    }
}
