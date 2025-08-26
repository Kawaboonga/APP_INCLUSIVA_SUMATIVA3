package com.example.ref01.ui.screens


import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.ref01.data.UserRepository
import com.example.ref01.ui.theme.Ref01Theme
import com.example.ref01.ui.utils.Dimens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegistered: () -> Unit // ⬅️ NUEVO: callback para navegar a Login
) {
    // Campos requeridos
    var username by remember { mutableStateOf("") }     // nombre de usuario
    var password by remember { mutableStateOf("") }     // contraseña
    var firstName by remember { mutableStateOf("") }    // nombre
    var lastName by remember { mutableStateOf("") }     // apellido
    var email by remember { mutableStateOf("") }        // correo
    // dropdown
    val countries = listOf("Chile", "Argentina", "México")
    var countryExpanded by remember { mutableStateOf(false) }
    var countrySelected by remember { mutableStateOf(countries.first()) }

    // Terminos - condicion pata que pase el login
    var acceptTerms by remember { mutableStateOf(false) }

    // Feedback
    var feedback by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.ScreenPadding)
            .semantics { contentDescription = "Pantalla de Registro" },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro de usuario (máx. 5)", style = MaterialTheme.typography.titleLarge)
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

        // Pais
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

        // Checkbox
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

        // Guardar
        Button(
            onClick = {
                if (!acceptTerms) {
                    feedback = "Debes aceptar términos"
                } else {
                    UserRepository.addUser(
                        username = username,
                        password = password,
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        country = countrySelected
                    )
                        .onSuccess { created ->
                            // Mensaje en pantalla con valores para la demo de asignatura
                            feedback = "Usuario registrado con éxito, muestra datos para la demo de asignatura \n" +
                                    "username = \"${created.username}\"\n" +
                                    "password = \"${created.password}\""

                            // Toast
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()

                            // Espera y pasa al Login
                           
                            scope.launch {
                                delay(2000)
                                onRegistered()
                            }
                        }
                        .onFailure { e ->
                            feedback = e.message ?: "Error al registrar"
                        }
                }
            },
            enabled = username.isNotBlank() && password.isNotBlank()
                    && firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank(),
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

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 0.dp, max = 220.dp)
        ) {
            items(UserRepository.list()) { u ->
                ListItem(
                    headlineContent = { Text("${u.firstName} ${u.lastName} (${u.username})") },
                    supportingContent = { Text("${u.email} • ${u.country}") }
                )
                Divider()
            }
        }
    }
}

@Preview(name = "Registro Claro", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Registro Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewRegister() {
    Ref01Theme(dynamicColor = false) { // para ver bien los colores por defecto
        RegisterScreen(onBack = {}, onRegistered = {})
    }
}
