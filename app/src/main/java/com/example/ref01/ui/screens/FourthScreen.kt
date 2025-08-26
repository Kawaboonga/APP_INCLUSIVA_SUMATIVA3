package com.example.ref01.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.ref01.R
import com.example.ref01.ui.components.DrawScreen
import com.example.ref01.ui.theme.Ref01Theme
import com.example.ref01.ui.utils.Dimens

@Composable
fun FourthScreen(
    onFinish: () -> Unit,
    onBack: () -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
            .semantics { contentDescription = "Pantalla Fourth" },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // imagen
        DrawScreen(
            rawRes = R.raw.backtoschool
        )
        Text(
            text = "Cuarto paso",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(Dimens.BetweenItems))
        Text(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua..",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(Dimens.BetweenItems))

        /*Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight)
                .semantics { contentDescription = "Botón Finalizar" }
        ) { Text("Finalizar") }*/

       // Spacer(Modifier.height(8.dp))

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight)
                .semantics { contentDescription = "Botón Volver" }
        ) { Text("Volver") }
    }
}

@Preview(name = "Fourth Claro", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Fourth Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewFourth() {
    Ref01Theme { FourthScreen(onFinish = {}, onBack = {}) }
}
