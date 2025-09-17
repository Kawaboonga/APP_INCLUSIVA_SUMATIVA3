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
import com.example.ref01.data.UserRepository
import com.example.ref01.ui.components.DrawScreen
import com.example.ref01.ui.theme.Purple
import com.example.ref01.ui.theme.PurpleDark
import com.example.ref01.ui.theme.Ref01Theme
import com.example.ref01.ui.theme.PurpleMedium // <-- asegúrate de tener este color
import com.example.ref01.ui.utils.Dimens

@Composable
fun LoginScreen(
    onGoRegister: () -> Unit,
    onGoForgot: () -> Unit,
    onLoginSuccess: () -> Unit = {}
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var message  by remember { mutableStateOf("") }
    val focus = LocalFocusManager.current

    val isEnabled = username.isNotBlank() && password.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple) // fondo general morado
            .semantics { contentDescription = "Pantalla de Login" }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen más grande
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

            // Tarjeta SOLO para inputs
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
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions { focus.clearFocus() },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (UserRepository.validate(username, password)) {
                                message = "Ingreso correcto ✅"
                                focus.clearFocus()
                                onLoginSuccess()
                            } else {
                                message = "Usuario o contraseña incorrectos"
                                focus.clearFocus()
                            }
                        },
                        enabled = isEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            // reposo (deshabilitado): PurpleDark, activo (habilitado): Purple
                            containerColor = if (isEnabled) Purple else PurpleDark.copy(alpha = 0.65f),
                            contentColor = Color.White,
                            disabledContainerColor = PurpleDark.copy(alpha = 0.40f),
                            disabledContentColor = Color.White.copy(alpha = 0.85f)
                        )
                    ) { Text("Ingresar", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                }
            }

            // Empuja ~40% del alto hacia abajo las acciones secundarias
            Spacer(Modifier.weight(0.4f))

            // Botones blancos (olvido / registro)
            Button(
                onClick = onGoForgot,
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

            // Enlace "Usar cuenta demo" como TEXTO al final
            TextButton(
                onClick = {
                    username = UserRepository.DEFAULT_USERNAME
                    password = UserRepository.DEFAULT_PASSWORD
                    message = "Cuenta demo cargada (${UserRepository.DEFAULT_USERNAME}/${UserRepository.DEFAULT_PASSWORD})"
                }
            ) { Text("Usar cuenta demo (${UserRepository.DEFAULT_USERNAME}/${UserRepository.DEFAULT_PASSWORD})", color = Color.White) }

            if (message.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(message, color = Color.White, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

