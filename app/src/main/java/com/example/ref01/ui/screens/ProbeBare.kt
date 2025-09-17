package com.example.ref01.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // IMPORTANTE
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProbeBare() {
    Scaffold(topBar = { Text("Bare Probe") }) { inner ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(inner)
        ) {
            // Opción con lista: necesitas el import de 'items' de Lazy
            items((1..200).toList()) { i ->
                Text("Ítem $i", modifier = Modifier.padding(16.dp))
            }

            // (Alternativa equivalente con 'count')
            // items(count = 200) { idx ->
            //     Text("Ítem ${idx + 1}", modifier = Modifier.padding(16.dp))
            // }
        }
    }
}
