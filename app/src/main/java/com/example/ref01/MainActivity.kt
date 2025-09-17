package com.example.ref01

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ref01.core.tts.LocalReadAloud
import com.example.ref01.core.tts.ReadAloudViewModel
import com.example.ref01.navigation.AppNavigation
import com.example.ref01.ui.theme.Ref01Theme


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text


/**class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProbeBare()    // <- SOLO esto, temporalmente
        }
    }
}

@Composable
fun ProbeBare() {
    Scaffold(topBar = { Text("Bare Probe") }) { inner ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(inner)
        ) {
            items((1..200).toList()) { i ->
                Text("Ítem $i", modifier = Modifier.padding(16.dp))
            }
        }
    }
} **/


/** **/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Ref01Theme(
                darkTheme = false
            ) {
                val navController = rememberNavController()

                // crea el VM que implementa ReadAloud
                val readerVm: ReadAloudViewModel = viewModel()

                CompositionLocalProvider(LocalReadAloud provides readerVm) {
                    AppNavigation(navController)
                }
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
    Ref01Theme(darkTheme = false) {
        val navController = rememberNavController()
        AppNavigation(navController) // preview con navController inyectado
    }
}

@Preview(
    name = "MainActivity - Oscuro",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun PreviewMainActivityDark() {
    Ref01Theme(darkTheme = false) {
        val navController = rememberNavController()
        AppNavigation(navController)
    }
}

