// ui/screens/settings/SettingsScreen.kt
package com.example.ref01.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ref01.navigation.routes.Screen
import com.example.ref01.ui.components.ScreenScaffold

@Composable
fun SettingsScreen(navController: NavController) {
    ScreenScaffold(
        title = "Configuración",
        canNavigateBack = true,
        onBack = { navController.popBackStack() },
        paragraphProvider = { listOf("Configuración de la aplicación") },
        topBarColor = MaterialTheme.colorScheme.primary,
        navController = navController
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // --- Buscar dispositivo ---
            ListItem(
                headlineContent = { Text("Buscar dispositivo") },
                supportingContent = { Text("Ver ubicación, compartir y hacer sonar el teléfono") },
                leadingContent = { Icon(Icons.Filled.LocationSearching, contentDescription = null) },
                modifier = Modifier.clickable { navController.navigate(Screen.DeviceFinder.route) }
            )
            Divider()

            // --- Opcional: Acerca de ---
            ListItem(
                headlineContent = { Text("Acerca de") },
                supportingContent = { Text("Versión y créditos") },
                leadingContent = { Icon(Icons.Filled.Info, contentDescription = null) }
            )
            Divider()

            // --- Opcional: Ajustes avanzados (placeholder) ---
            ListItem(
                headlineContent = { Text("Ajustes avanzados") },
                supportingContent = { Text("Opciones adicionales de la app") },
                leadingContent = { Icon(Icons.Filled.Build, contentDescription = null) }
            )
        }
    }
}
