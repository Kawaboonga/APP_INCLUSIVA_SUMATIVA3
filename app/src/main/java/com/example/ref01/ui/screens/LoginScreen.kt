package com.example.ref01.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ref01.R
import com.example.ref01.data.local.DBHelper
import com.example.ref01.data.local.UserLocalRepository
import com.example.ref01.ui.components.DrawScreen
import com.example.ref01.ui.theme.Purple
import com.example.ref01.ui.theme.PurpleDark
import com.example.ref01.ui.theme.Ref01Theme
import com.example.ref01.ui.utils.Dimens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(
    onGoRegister: () -> Unit,
    onGoForgot: () -> Unit,
    onLoginSuccess: () -> Unit = {}
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var message  by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val focus = LocalFocusManager.current
    val ctx = LocalContext.current
    val userRepo = remember { UserLocalRepository(ctx) }
    val scope = rememberCoroutineScope()

    val isEnabled = username.isNotBlank() && password.isNotBlank() && !isLoading

    fun doLogin(uRaw: String, pRaw: String) {
        val u = uRaw.trim()
        val p = pRaw.trim()
        if (u.isEmpty() || p.isEmpty()) return

        isLoading = true
        message = ""
        scope.launch {
            val result = withContext(Dispatchers.IO) { userRepo.login(u, p) }
            result
                .onSuccess {
                    message = "Ingreso correcto ✅"
                    focus.clearFocus()
                    onLoginSuccess()
                }
                .onFailure { e ->
                    // Mensaje uniforme y claro
                    message = e.message ?: "Usuario o contraseña incorrectos"
                    focus.clearFocus()
                }
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple)
            .semantics { contentDescription = "Pantalla de Login" }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animación/ilustración superior
            DrawScreen(
                rawRes = R.raw.backtoschool,
                modifier = Modifier.fillMaxWidth(),
                height = 320.dp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "BOOK EASY",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9FB)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Usuario") },
                        singleLine = true,
                        enabled = !isLoading,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        enabled = !isLoading,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions {
                            focus.clearFocus()
                            if (isEnabled) doLogin(username, password)
                        },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { doLogin(username, password) },
                        enabled = isEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEnabled) Purple else PurpleDark.copy(alpha = 0.65f),
                            contentColor = Color.White,
                            disabledContainerColor = PurpleDark.copy(alpha = 0.40f),
                            disabledContentColor = Color.White.copy(alpha = 0.85f)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                        } else {
                            Text("Ingresar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(Modifier.weight(0.4f))

            Button(
                onClick = onGoForgot,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Purple
                )
            ) { Text("¿Olvidaste tu contraseña?", fontWeight = FontWeight.SemiBold) }

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = onGoRegister,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Purple
                )
            ) { Text("Crear cuenta (Registro)", fontWeight = FontWeight.SemiBold) }

            Spacer(Modifier.height(12.dp))

            TextButton(
                onClick = {
                    username = DBHelper.DEFAULT_USERNAME
                    password = DBHelper.DEFAULT_PASSWORD
                    message = "Cuenta demo cargada (${DBHelper.DEFAULT_USERNAME}/${DBHelper.DEFAULT_PASSWORD})"
                    doLogin(username, password)
                },
                enabled = !isLoading
            ) {
                Text(
                    "Usar cuenta demo (${DBHelper.DEFAULT_USERNAME}/${DBHelper.DEFAULT_PASSWORD})",
                    color = Color.White
                )
            }

            if (message.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(message, color = Color.White, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
