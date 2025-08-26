package com.example.ref01.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ref01.data.UserRepository
import com.example.ref01.ui.theme.Ref01Theme
import com.example.ref01.ui.utils.Dimens

@Composable
fun LoginScreen(
    onGoRegister: () -> Unit,
    onGoForgot: () -> Unit,
    onLoginSuccess: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val focus = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
            .semantics { contentDescription = "Pantalla de Login" },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Accesibilidad — Login", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(Dimens.BetweenItems))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo Usuario" }
        )
        Spacer(Modifier.height(Dimens.BetweenItems))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo Contraseña" }
        )
        Spacer(Modifier.height(Dimens.BetweenItems))

        Button(
            onClick = {
                val ok = UserRepository.validate(username, password)
                if (ok) {
                    message = "Ingreso correcto ✅"
                    onLoginSuccess()        //  al flujo
                } else {
                    message = "Usuario o contraseña incorrectos"
                }
                focus.clearFocus()
            },
            enabled = username.isNotBlank() && password.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight)
                .semantics { contentDescription = "Botón Ingresar" }
        ) { Text("Ingresar") }

        Spacer(Modifier.height(8.dp))

        TextButton(
            onClick = onGoForgot,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Enlace Recuperar contraseña" }
        ) { Text("¿Olvidaste tu contraseña?") }

        TextButton(
            onClick = onGoRegister,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Enlace Ir a Registro" }
        ) { Text("Crear cuenta (Registro)") }

        Spacer(Modifier.height(Dimens.BetweenItems))
        if (message.isNotBlank()) Text(message)
    }
}

@Preview(name = "Login Claro", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Login Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewLogin() {
    Ref01Theme { LoginScreen(onGoRegister = {}, onGoForgot = {}) }
}