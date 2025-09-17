package com.example.ref01.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ref01.R
import com.example.ref01.data.BookRepository
import com.example.ref01.ui.components.PrimaryButton
import com.example.ref01.ui.components.ScreenScaffold
import androidx.compose.material3.LocalTextStyle
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.unit.sp

@Composable
fun BookDetailScreen(navController: NavController, bookId: Int) {
    val book = remember(bookId) { BookRepository.getById(bookId) }

    // Escala de fuente local a esta pantalla
    var fontScale by rememberSaveable { mutableFloatStateOf(1.0f) }
    val minScale = 0.9f
    val maxScale = 1.6f
    fun inc() { fontScale = (fontScale + 0.1f).coerceAtMost(maxScale) }
    fun dec() { fontScale = (fontScale - 0.1f).coerceAtLeast(minScale) }
    fun reset() { fontScale = 1.0f }

    // Mínimo 18sp helper
    @Composable
    fun min18sp(valueSp: Float) = with(LocalTextStyle.current) {
        if (valueSp < 18f) 18.sp else valueSp.sp
    }

    if (book == null) {
        ScreenScaffold(
            title = "Detalle del libro",
            canNavigateBack = true,
            onBack = { navController.popBackStack() },
            paragraphProvider = { emptyList() }
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No se encontró el libro solicitado.", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    PrimaryButton(text = "Volver", onClick = { navController.popBackStack() })
                }
            }
        }
        return
    }

    val paragraphs = remember(book) { book.paragraphs() }

    // Lo que leerá el TTS (título + autor + resumen)
    val ttsParagraphs = remember(book) {
        buildList {
            add(book.title.ifBlank { "Título desconocido" })
            add("Autor: " + book.author.ifBlank { "Desconocido" })
            // Si quieres, también:
            // add("Editorial: " + book.publisher.ifBlank { "Desconocida" })
            addAll(paragraphs) // el resumen por párrafos
        }
    }

    val context = LocalContext.current

    ScreenScaffold(
        title = "",
        canNavigateBack = true,
        onBack = { navController.popBackStack() },
        // 👇 ahora el TopBar leerá título + autor + resumen
        paragraphProvider = { ttsParagraphs },
        showTitle = false,
        showBackLabel = true,
        backLabel = "Volver"
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .verticalScroll(rememberScrollState())
            ) {
                // Controles A− / A / A+
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(onClick = { dec() }) { Text("A−") }
                    OutlinedButton(onClick = { reset() }) { Text("A") }
                    FilledTonalButton(onClick = { inc() }) { Text("A+") }
                }

                // Imagen con bordes redondeados
                val model = ImageRequest.Builder(context)
                    .data(if (book.imageUrl.isBlank()) R.drawable.ic_book_placeholder else book.imageUrl)
                    .crossfade(true)
                    .build()

                AsyncImage(
                    model = model,
                    contentDescription = "Portada de ${book.title.ifBlank { "Libro" }}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.ic_book_placeholder),
                    error = painterResource(R.drawable.ic_book_placeholder),
                    fallback = painterResource(R.drawable.ic_book_placeholder)
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = book.title.ifBlank { "Título desconocido" },
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = min18sp(MaterialTheme.typography.headlineSmall.fontSize.value * fontScale),
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Autor: " + book.author.ifBlank { "Desconocido" },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = min18sp(MaterialTheme.typography.bodyLarge.fontSize.value * fontScale)
                    )
                )
                Text(
                    text = "Editorial: " + book.publisher.ifBlank { "Desconocida" },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = min18sp(MaterialTheme.typography.bodyMedium.fontSize.value * fontScale)
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Divider(Modifier.padding(vertical = 16.dp))

                if (paragraphs.isEmpty()) {
                    Text(
                        text = "No hay resumen disponible para este libro.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = min18sp(MaterialTheme.typography.bodyLarge.fontSize.value * fontScale)
                        )
                    )
                } else {
                    paragraphs.forEachIndexed { idx, p ->
                        Text(
                            text = p,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = min18sp(MaterialTheme.typography.bodyLarge.fontSize.value * fontScale)
                            )
                        )
                        if (idx != paragraphs.lastIndex) Spacer(Modifier.height(12.dp))
                    }
                }

                Spacer(Modifier.height(80.dp)) // espacio para el botón fijo
            }

            // Botón fijo inferior
            PrimaryButton(
                text = "Volver al Home",
                onClick = { navController.popBackStack("home", inclusive = false) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
            )
        }
    }
}
