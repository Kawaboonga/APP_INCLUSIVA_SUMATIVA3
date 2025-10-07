package com.example.ref01.ui.screens

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ref01.ui.theme.Ref01Theme
import com.example.ref01.ui.utils.Dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// 🔄 Nuevo: SQLite repo + DB helper para listar/contar usuarios
import com.example.ref01.data.local.DBHelper
import com.example.ref01.data.local.UserLocalRepository

// Modelo simple para mostrar en la lista (evitamos exponer entidades internas)
private data class UiUser(
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val country: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegistered: () -> Unit
) {
    // Campos requeridos
    var username  by rememberSaveable { mutableStateOf("") }
    var password  by rememberSaveable { mutableStateOf("") }
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName  by rememberSaveable { mutableStateOf("") }
    var email     by rememberSaveable { mutableStateOf("") }

    //  País con dropdown
    val countries = listOf("Chile", "Argentina", "México")
    var countryExpanded by rememberSaveable { mutableStateOf(false) }
    var countrySelected by rememberSaveable { mutableStateOf(countries.first()) }

    // Aceptar términos
    var acceptTerms by rememberSaveable { mutableStateOf(false) }

    // Feedback
    var feedback by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // 🔄 Nuevo: repo SQLite
    val userRepo = remember { UserLocalRepository(context) }

    // 🔄 Nuevo: listado de usuarios desde la BD
    var users by remember { mutableStateOf(listOf<UiUser>()) }
    var total by remember { mutableStateOf(0) }
    val MAX_USERS = 100 // soft-cap de la demo; cambia a Int.MAX_VALUE si no lo quieres

    suspend fun reloadUsers() {
        val (list, count) = withContext(Dispatchers.IO) {
            // Consultamos directo la DB para listado y conteo
            val db = DBHelper(context).readableDatabase
            val items = mutableListOf<UiUser>()
            var c = 0
            db.rawQuery(
                "SELECT username, first_name, last_name, email, country FROM users ORDER BY id DESC",
                emptyArray()
            ).use { cur ->
                while (cur.moveToNext()) {
                    val u = UiUser(
                        username = cur.getString(0),
                        firstName = cur.getString(1),
                        lastName = cur.getString(2),
                        email = cur.getString(3),
                        country = cur.getString(4)
                    )
                    items.add(u)
                }
                c = cur.count
            }
            items to c
        }
        users = list
        total = count
    }

    // Cargar al entrar
    LaunchedEffect(Unit) { reloadUsers() }

    val hasCapacity = total < MAX_USERS

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
            .semantics { contentDescription = "Pantalla de Registro" },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro de usuario (máx. $MAX_USERS)", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(4.dp))
        Text(
            "Registrados: $total  •  Restantes: ${MAX_USERS - total}",
            style = MaterialTheme.typography.bodySmall,
            color = if (hasCapacity) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )

        Spacer(Modifier.height(Dimens.BetweenItems))

        // Usuario
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de usuario") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo Nombre de usuario" }
        )
        Spacer(Modifier.height(Dimens.BetweenItems))

        // Nombre
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo Nombre" }
        )

        Spacer(Modifier.height(Dimens.BetweenItems))

        // Apellido
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Apellido") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo Apellido" }
        )
        Spacer(Modifier.height(Dimens.BetweenItems))

        // Correo
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Campo Correo" }
        )

        Spacer(Modifier.height(Dimens.BetweenItems))

        // Contraseña
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

        // País
        ExposedDropdownMenuBox(
            expanded = countryExpanded,
            onExpandedChange = { countryExpanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = countrySelected,
                onValueChange = {},
                readOnly = true,
                label = { Text("País") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .semantics { contentDescription = "Selector de país" }
            )
            ExposedDropdownMenu(
                expanded = countryExpanded,
                onDismissRequest = { countryExpanded = false }
            ) {
                countries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            countrySelected = option
                            countryExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(Dimens.BetweenItems))

        // Checkbox términos
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Checkbox(
                checked = acceptTerms,
                onCheckedChange = { acceptTerms = it },
                modifier = Modifier.semantics {
                    contentDescription = "Aceptar términos y condiciones"
                    role = Role.Checkbox
                }
            )
            Spacer(Modifier.width(8.dp))
            Text("Acepto términos y condiciones")
        }

        Spacer(Modifier.height(Dimens.BetweenItems))

        // BTN Guardar (ahora usando SQLite)
        Button(
            onClick = {
                when {
                    !hasCapacity -> {
                        feedback = "Máximo $MAX_USERS usuarios para la demo."
                    }
                    !acceptTerms -> {
                        feedback = "Debes aceptar términos"
                    }
                    username.isBlank() || password.isBlank()
                            || firstName.isBlank() || lastName.isBlank() || email.isBlank() -> {
                        feedback = "Completa todos los campos"
                    }
                    else -> {
                        // Registrar en BD (IO)
                        scope.launch {
                            val res = withContext(Dispatchers.IO) {
                                userRepo.register(
                                    username = username,
                                    password = password,
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email,
                                    country = countrySelected
                                )
                            }
                            res.onSuccess {
                                feedback = """
                                    Usuario registrado con éxito
                                    username = "$username"
                                    password = "$password"
                                    Total: ${total + 1}/$MAX_USERS
                                """.trimIndent()

                                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()

                                // limpiar campos
                                username = ""; password = ""; firstName = ""; lastName = ""; email = ""
                                acceptTerms = false

                                // recargar listado
                                reloadUsers()

                                // pequeña pausa y volver
                                scope.launch {
                                    delay(1200)
                                    onRegistered()
                                }
                            }.onFailure { e ->
                                feedback = e.message ?: "Error al registrar (usuario/correo ya existe)"
                            }
                        }
                    }
                }
            },
            enabled = hasCapacity,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight)
                .semantics { contentDescription = "Botón Guardar usuario" }
        ) { Text("Guardar") }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.ButtonHeight)
        ) { Text("Volver") }

        Spacer(Modifier.height(Dimens.BetweenItems))
        if (feedback.isNotBlank()) Text(feedback)

        Spacer(Modifier.height(Dimens.BetweenItems))
        Text("Usuarios registrados:", style = MaterialTheme.typography.titleMedium)

        // 🔄 Lista de usuarios desde SQLite
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 0.dp, max = 220.dp)
        ) {
            items(users) { u ->
                ListItem(
                    headlineContent = { Text("${(u.firstName ?: "").ifBlank { "Nombre" }} ${(u.lastName ?: "").ifBlank { "" }} (${u.username})") },
                    supportingContent = { Text("${u.email ?: "—"} • ${u.country ?: "—"}") }
                )
                Divider()
            }
        }
    }
}

// Previews
@Preview(name = "Registro Claro", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Registro Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewRegister() {
    Ref01Theme {
        RegisterScreen(onBack = {}, onRegistered = {})
    }
}
