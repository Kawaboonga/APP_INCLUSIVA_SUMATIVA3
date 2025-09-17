// ScreenScaffold.kt
package com.example.ref01.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    title: String,
    canNavigateBack: Boolean,
    onBack: (() -> Unit)? = null,
    paragraphProvider: () -> List<String>,
    topBarColor: Color = MaterialTheme.colorScheme.primary,
    topBarContentColor: Color = Color.White,      // nuevo: color del contenido del TopBar
    topBarIconSize: Dp = 28.dp,
    onDecFont: (() -> Unit)? = null,
    onIncFont: (() -> Unit)? = null,
    showTitle: Boolean = true,
    showBackLabel: Boolean = false,
    backLabel: String = "Volver",
    useScrollBehavior: Boolean = false,          // nuevo: activa nested scroll del TopBar
    content: @Composable () -> Unit
) {
    // scroll behavior opcional (para listas/scroll en la pantalla)
    val scrollBehavior: TopAppBarScrollBehavior? =
        if (useScrollBehavior) TopAppBarDefaults.pinnedScrollBehavior() else null

    Scaffold(
        modifier = if (scrollBehavior != null) Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) else Modifier,
        topBar = {
            AppTopBar(
                title = title,
                canNavigateBack = canNavigateBack,
                onBack = onBack,
                paragraphProvider = paragraphProvider,
                containerColor = topBarColor,
                contentColor = topBarContentColor,
                iconSize = topBarIconSize,
                onDecFont = onDecFont,
                onIncFont = onIncFont,
                showTitle = showTitle,
                showBackLabel = showBackLabel,
                backLabel = backLabel,
                scrollBehavior = scrollBehavior
            )
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
        ) { content() }
    }
}




        /**
         *

fun ScreenScaffold(
    title: String,
    canNavigateBack: Boolean,
    onBack: (() -> Unit)? = null,
    paragraphProvider: () -> List<String>,
    // ✅ defaults para no romper llamadas existentes
    topBarColor: Color = MaterialTheme.colorScheme.primary,
    topBarIconSize: Dp = 28.dp,
    content: @Composable () -> Unit
         **/

/**
 * fun ScreenScaffold(
    title: String,
    canNavigateBack: Boolean,
    onBack: (() -> Unit)? = null,
    paragraphProvider: () -> List<String>,
    topBarColor: Color = MaterialTheme.colorScheme.primary,
    topBarIconSize: Dp = 28.dp,
    onDecFont: (() -> Unit)? = null,
    onIncFont: (() -> Unit)? = null,
    showTitle: Boolean = true,
    showBackLabel: Boolean = false,
    backLabel: String = "Volver",
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = title,
                canNavigateBack = canNavigateBack,
                onBack = onBack,
                paragraphProvider = paragraphProvider,
                containerColor = topBarColor,
                iconSize = topBarIconSize,
                onDecFont = onDecFont,
                onIncFont = onIncFont,
                showTitle = showTitle,
                showBackLabel = showBackLabel,
                backLabel = backLabel
            )
        }
    ) { inner -> Box(Modifier.fillMaxSize().padding(inner)) { content() } }
}
**/


/** Scaffold que inserta AppTopBar (versión simple, sin nestedScroll)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    title: String,
    canNavigateBack: Boolean,
    onBack: (() -> Unit)? = null,
    paragraphProvider: () -> List<String>,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = title,
                canNavigateBack = canNavigateBack,
                onBack = onBack,
                paragraphProvider = paragraphProvider
            )
        }
    ) { inner ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(inner)          // respeta el espacio de la TopBar
                .navigationBarsPadding() // evita solaparse con gestos/sistema
                .imePadding()            // sube contenido cuando aparece el teclado
        ) {
            content()
        }
    }
}
 **/