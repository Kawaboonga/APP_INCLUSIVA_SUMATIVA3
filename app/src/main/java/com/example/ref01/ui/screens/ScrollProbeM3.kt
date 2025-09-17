package com.example.ref01.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollProbeM3() {
    val behavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(behavior.nestedScrollConnection),
        topBar = { TopAppBar(title = { Text("Probe M3") }, scrollBehavior = behavior) }
    ) { inner ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(inner)
        ) {
            items((1..100).toList()) { i ->
                Text("Ítem $i", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
