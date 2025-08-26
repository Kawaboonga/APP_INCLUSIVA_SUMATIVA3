package com.example.ref01

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.ref01.navigation.AppNavigation
import com.example.ref01.ui.theme.Ref01Theme
import com.example.ref01.ui.utils.Dimens

/**
 * MainActivity queda INTENCIONALMENTE LIMPIA.
 * - Solo enciende el tema y el NavHost.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Ref01Theme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Preview(
    name = "MainActivity - Claro",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
private fun PreviewMainActivityLight() {
    Ref01Theme(
        darkTheme = false,
        dynamicColor = false // 🚨 clave: desactivar dynamic color
    ) {
        val navController = rememberNavController()
        AppNavigation(navController)
    }
}

@Preview(
    name = "MainActivity - Oscuro",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun PreviewMainActivityDark() {
    Ref01Theme(
        darkTheme = true,
        dynamicColor = false // 🚨 clave: desactivar dynamic color
    ) {
        val navController = rememberNavController()
        AppNavigation(navController)
    }
}