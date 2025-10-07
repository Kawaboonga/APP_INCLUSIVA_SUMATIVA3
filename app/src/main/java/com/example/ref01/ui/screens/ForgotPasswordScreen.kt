package com.example.ref01.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ref01.ui.theme.Ref01Theme
import com.example.ref01.ui.utils.Dimens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Nuevo: repositorio SQLite
import com.example.ref01.data.local.UserLocalRepository

@Composable
fun ForgotPasswordScreen(onBack: () -> Unit) {
    var username by rememberSaveable { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val ctx = LocalContext.current
    val userRepo = remember { UserLocalRepository(ctx) }
    val scope = rememberCoroutineScope()

    fun doRecover() {
        isLoading = true
        message = ""
        scope.launch {
            val result = withContext(Dispatchers.IO) { userRepo.recoverPassword(username) }
            result
                .onSuccess { email ->
                    // Demo: no enviamos correo real; solo mostramos el email registrado.
                    message = "Si el usuario existe, enviaremos instrucciones a: $email"
                }
                .onFailure { e ->
                    message = e.message ?: "Usuario no encontrado."
                }
            isLoading = false
        }
    }

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
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo Usuario Recuperar" }
        )

        Spacer(Modifier.height(Dimens.BetweenItems))

        Button(
            onClick = { doRecover() },
            enabled = username.isNotBlank() && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight)
                .semantics { contentDescription = "Botón Recuperar" }
        ) {
            if (isLoading) {
                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
            } else {
                Text("Recuperar")
            }
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBack,
            enabled = !isLoading,
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
