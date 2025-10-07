// FirstScreen.kt
package com.example.ref01.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ref01.R
import com.example.ref01.ui.components.ScreenScaffold
import com.example.ref01.ui.components.StepScreenLayout
import com.example.ref01.ui.theme.PurpleMedium
import com.example.ref01.ui.theme.Ref01Theme
import com.example.ref01.ui.utils.Dimens
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.ref01.navigation.routes.Screen // ajusta si tu objeto/ruta está en otro paquete




@Composable
fun FirstScreen(
    navController: NavController,
    onNext: () -> Unit,
    onBack: () -> Unit = {}
) {
    val titulo = "Primer paso"
    val cuerpo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.."
    val paragraphProvider = remember(titulo, cuerpo) { { listOf(titulo, cuerpo) } }

    ScreenScaffold(
        title = titulo,
        canNavigateBack = false,     // sin flecha en TopBar
        onBack = {},
        //paragraphProvider = paragraphProvider,
        paragraphProvider = { emptyList() },
        topBarColor = PurpleMedium,
        navController = navController,
        showOverflowMenu = false    //  oculta ⋮
    ) {
        StepScreenLayout(
            title = titulo,
            body = cuerpo,
            onPrimary = onNext,
            //  Ir SIEMPRE a Home (aunque no esté en el back stack)
            onSecondary = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            primaryText = "Siguiente",
            secondaryText = "Ir al Home",
            showSecondary = true
        )
    }
}

/**
@Composable
fun FirstScreen(
    navController: NavController,
    onNext: () -> Unit,
    onBack: () -> Unit = {}
) {
    val titulo = "Primer paso"
    val cuerpo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.."
    val paragraphProvider = remember(titulo, cuerpo) { { listOf(titulo, cuerpo) } }

    ScreenScaffold(
        title = titulo,
        canNavigateBack = true,
        onBack = onBack,
        paragraphProvider = paragraphProvider,
        topBarColor = PurpleMedium
    ) {
        StepScreenLayout(
            title = titulo,
            body = cuerpo,
            onPrimary = onNext,
            showSecondary = false   //  Oculta botón Volver
        )
    }
}
**/

// Preview opcional (sin NavController real)
@Preview(name = "First Claro", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "First Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewFirst() {
    Ref01Theme {
        // StepScreenLayout(title = "Primer paso", body = "…", onPrimary = {}, onSecondary = {})
    }
}
