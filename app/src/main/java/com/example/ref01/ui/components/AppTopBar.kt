package com.example.ref01.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag // 👈 IMPORTANTE
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ref01.core.tts.LocalReadAloud
import com.example.ref01.navigation.routes.Screen
import com.example.ref01.ui.theme.LocalThemeController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    canNavigateBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    paragraphProvider: () -> List<String> = { emptyList() },
    containerColor: Color,
    contentColor: Color = Color.White,
    iconSize: Dp = 28.dp,
    onDecFont: (() -> Unit)? = null,
    onIncFont: (() -> Unit)? = null,
    showTitle: Boolean = true,
    showBackLabel: Boolean = false,
    backLabel: String = "Volver",
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navController: NavController? = null,
    showOverflowMenu: Boolean = true,
    showUserAction: Boolean = true
) {
    val reader = LocalReadAloud.current
    val themeCtl = LocalThemeController.current
    val currentNav by rememberUpdatedState(navController)

    var isReading by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    val rotation by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (themeCtl.isDark) 180f else 0f, label = "ThemeIconRotate"
    )

    TopAppBar(
        title = {
            if (showTitle) {
                Text(
                    text = title,
                    color = contentColor,
                    modifier = Modifier.testTag("topbar_title")
                )
            }
        },
        navigationIcon = {
            if (canNavigateBack && onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("nav_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = if (showBackLabel) backLabel else "Volver",
                        tint = contentColor
                    )
                }
            }
        },
        actions = {
            // Toggle de tema
            IconButton(
                onClick = themeCtl.toggle,
                modifier = Modifier.testTag("theme_toggle_btn")
            ) {
                Icon(
                    imageVector = if (themeCtl.isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                    contentDescription = if (themeCtl.isDark) "Cambiar a tema claro" else "Cambiar a tema oscuro",
                    tint = contentColor,
                    modifier = Modifier.rotate(rotation)
                )
            }

            // Play <-> Pausa (TTS)
            if (!isReading) {
                IconButton(
                    onClick = {
                        reader.readParagraphs(paragraphProvider())
                        isReading = true
                    },
                    modifier = Modifier.testTag("tts_play_btn")
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Reproducir", tint = contentColor)
                }
            } else {
                IconButton(
                    onClick = {
                        reader.pause()
                        isReading = false
                    },
                    modifier = Modifier.testTag("tts_pause_btn")
                ) {
                    Icon(Icons.Filled.Pause, contentDescription = "Pausar", tint = contentColor)
                }
            }

            // Perfil de usuario
            if (showUserAction) {
                IconButton(
                    onClick = { currentNav?.navigate(Screen.UserProfile.route) },
                    modifier = Modifier.testTag("user_profile_btn")
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Perfil de usuario",
                        tint = contentColor
                    )
                }
            }

            // Overflow ⋮
            if (showOverflowMenu) {
                IconButton(
                    onClick = { showMenu = !showMenu },
                    modifier = Modifier.testTag("overflow_btn")
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones", tint = contentColor)
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.testTag("overflow_menu")
                ) {
                    DropdownMenuItem(
                        text = { Text("Detener lectura") },
                        onClick = {
                            showMenu = false
                            reader.stop()
                            isReading = false
                        },
                        modifier = Modifier.testTag("menu_stop_reading")
                    )
                    DropdownMenuItem(
                        text = { Text("Buscar dispositivo") },
                        onClick = {
                            showMenu = false
                            currentNav?.navigate(Screen.DeviceFinder.route)
                        },
                        modifier = Modifier.testTag("menu_device_finder")
                    )
                    DropdownMenuItem(
                        text = { Text("Configuración") },
                        onClick = {
                            showMenu = false
                            currentNav?.navigate(Screen.Settings.route)
                        },
                        modifier = Modifier.testTag("menu_settings")
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = contentColor,
            navigationIconContentColor = contentColor,
            actionIconContentColor = contentColor
        ),
        scrollBehavior = scrollBehavior,
        modifier = Modifier.testTag("app_top_bar")
    )
}
