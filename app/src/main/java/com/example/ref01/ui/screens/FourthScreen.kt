// ui/screens/FourthScreen.kt
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
fun FourthScreen(
    onFinish: () -> Unit, // Navega a Home (resuelto en AppNavigation)
    onBack: () -> Unit
) {
    val titulo = "Cuarto paso"
    val cuerpo = "Has completado la secuencia inicial. Puedes finalizar para ir al Home."

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
            onPrimary = onFinish,
            onSecondary = onBack,
            primaryText = "Finalizar",
            secondaryText = "Volver",
            showSecondary = true
        )
    }
}

@Preview(name = "Fourth Claro", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Fourth Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewFourth() {
    Ref01Theme {
        StepScreenLayout(
            title = "Cuarto paso",
            body = "Has completado la secuencia inicial. Puedes finalizar para ir al Home.",
            onPrimary = {},
            onSecondary = {},
            primaryText = "Finalizar",
            secondaryText = "Volver"
        )
    }
}
