package com.example.ref01.ui.screens


import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ref01.data.UserRepository
import com.example.ref01.ui.theme.Ref01Theme
import com.example.ref01.ui.utils.Dimens

@Composable
fun ForgotPasswordScreen(onBack: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
            .semantics { contentDescription = "Pantalla de Recuperar contraseña" },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Recuperar contraseña", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(Dimens.BetweenItems))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo Usuario Recuperar" }
        )

        Spacer(Modifier.height(Dimens.BetweenItems))

        Button(
            onClick = {
                val exists = UserRepository.exists(username)
                message = if (exists) {
                    "Si el usuario existe, recibirás instrucciones (demo)."
                } else "Usuario no encontrado."
            },
            enabled = username.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight)
                .semantics { contentDescription = "Botón Recuperar" }
        ) { Text("Recuperar") }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight)
        ) { Text("Volver") }

        Spacer(Modifier.height(Dimens.BetweenItems))
        if (message.isNotBlank()) Text(message)
    }
}

@Preview(name = "Recuperar Claro", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Recuperar Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewForgot() {
    Ref01Theme { ForgotPasswordScreen(onBack = {}) }
}
