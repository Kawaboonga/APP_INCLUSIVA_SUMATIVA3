package com.example.ref01.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ref01.ui.utils.Dimens
import androidx.navigation.NavGraph.Companion.findStartDestination



@Composable
fun StepScreenLayout(
    title: String,
    body: String,
    onPrimary: () -> Unit,
    onSecondary: () -> Unit = {},
    primaryText: String = "Siguiente",
    secondaryText: String = "Volver",
    showSecondary: Boolean = true,   // 👈 nuevo
    buttonsSpacing: Int = 16,
    buttonHeight: Int = 56
) {
    Box(Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 16.dp)) {

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.60f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = title,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onPrimary,
                modifier = Modifier.fillMaxWidth().height(buttonHeight.dp)
            ) { Text(primaryText) }

            if (showSecondary) { // 👈 condicional
                Spacer(Modifier.height(buttonsSpacing.dp))
                OutlinedButton(
                    onClick = onSecondary,
                    modifier = Modifier.fillMaxWidth().height(buttonHeight.dp)
                ) { Text(secondaryText) }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
