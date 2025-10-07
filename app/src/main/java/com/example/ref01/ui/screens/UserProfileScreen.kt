package com.example.ref01.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ref01.data.local.UserLocalRepository
import com.example.ref01.ui.utils.Dimens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun UserProfileScreen(onBack: () -> Unit) {
    val ctx = LocalContext.current
    val repo = remember { UserLocalRepository(ctx) }
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var rut       by remember { mutableStateOf("") }
    var country   by remember { mutableStateOf("") }
    var msg       by remember { mutableStateOf("") }
    var saving    by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val u = repo.currentUserDetails()
        if (u != null) {
            username  = u.username
            firstName = u.firstName
            lastName  = u.lastName
            email     = u.email
            rut       = u.rut.orEmpty()
            country   = u.country
        } else {
            msg = "Sin sesión activa"
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
    ) {
        Text("Mi perfil", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {},
            label = { Text("Usuario") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = rut,
            onValueChange = { rut = it },
            label = { Text("RUT") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = country,
            onValueChange = { country = it },
            label = { Text("País") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                saving = true; msg = ""
                scope.launch {
                    val res = withContext(Dispatchers.IO) {
                        repo.updateCurrentUser(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            rut = rut.ifBlank { null },
                            country = country
                        )
                    }
                    res.onSuccess { msg = "Datos guardados ✅" }
                        .onFailure { e -> msg = e.message ?: "Error al guardar" }
                    saving = false
                }
            },
            enabled = !saving,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight)
        ) {
            if (saving) CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
            else Text("Guardar cambios")
        }

        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight)
        ) { Text("Volver") }

        if (msg.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(msg)
        }
    }
}
