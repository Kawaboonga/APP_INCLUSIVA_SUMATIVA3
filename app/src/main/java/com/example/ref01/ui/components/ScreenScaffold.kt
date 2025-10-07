package com.example.ref01.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    title: String,
    canNavigateBack: Boolean,
    onBack: (() -> Unit)? = null,
    paragraphProvider: () -> List<String>,
    topBarColor: Color = MaterialTheme.colorScheme.primary,
    topBarContentColor: Color = Color.White,
    topBarIconSize: Dp = 28.dp,
    onDecFont: (() -> Unit)? = null,
    onIncFont: (() -> Unit)? = null,
    showTitle: Boolean = true,
    showBackLabel: Boolean = false,
    backLabel: String = "Volver",
    useScrollBehavior: Boolean = false,
    navController: androidx.navigation.NavController? = null,
    //  NUEVO: se puede ocultar el ⋮ por pantalla
    showOverflowMenu: Boolean = true,
    content: @Composable () -> Unit
) {
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
                scrollBehavior = scrollBehavior,
                navController = navController,
                showOverflowMenu = showOverflowMenu   //  pasa el flag
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
