package com.example.ref01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ref01.core.tts.LocalReadAloud
import com.example.ref01.core.tts.ReadAloudViewModel
import com.example.ref01.navigation.AppNavigation
import com.example.ref01.ui.theme.AnimatedRef01Theme
import com.example.ref01.ui.theme.LocalThemeController
import com.example.ref01.ui.theme.ThemeController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Estado de tema (persistente en cambios de config)
            var isDark by rememberSaveable { mutableStateOf(false) }

            // ViewModel global para TTS
            val readerVm: ReadAloudViewModel = viewModel()

            // Proveer CompositionLocals (tema + lector)
            CompositionLocalProvider(
                LocalThemeController provides ThemeController(
                    isDark = isDark,
                    toggle = { isDark = !isDark }
                ),
                LocalReadAloud provides readerVm
            ) {
                AnimatedRef01Theme(darkTheme = isDark) {
                    // Contenedor raíz con testTag para pruebas
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("main_root")
                    ) {
                        AppNavigation() // tu NavHost interno
                    }
                }
            }
        }
    }
}
