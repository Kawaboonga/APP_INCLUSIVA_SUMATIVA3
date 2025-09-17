package com.example.ref01.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScrollProbe() {
    Scaffold(topBar = { Text("Probe") }) { inner ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(inner)
        ) {
            items((1..100).toList()) { i ->
                Text("Ítem $i", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
