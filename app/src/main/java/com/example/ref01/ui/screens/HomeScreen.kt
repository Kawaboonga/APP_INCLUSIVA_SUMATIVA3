package com.example.ref01.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ref01.R
import com.example.ref01.data.BookRepository
import com.example.ref01.navigation.routes.Screen
import com.example.ref01.ui.components.ScreenScaffold
import androidx.compose.runtime.LaunchedEffect


@Composable
fun HomeScreen(navController: NavController) {
    val books = remember { BookRepository.getAll() }
    val listState = rememberLazyListState()

    // ===== Control de tipografía accesible (local a Home) =====
    var fontScale by rememberSaveable { mutableFloatStateOf(1.0f) }
    val minScale = 0.9f
    val maxScale = 1.6f
    fun inc() { fontScale = (fontScale + 0.1f).coerceAtMost(maxScale) }
    fun dec() { fontScale = (fontScale - 0.1f).coerceAtLeast(minScale) }
    fun reset() { fontScale = 1.0f }

    // 👇 EFECTO: cada vez que cambia fontScale, vuelve (animado) al inicio de la lista
    LaunchedEffect(fontScale) {
        listState.animateScrollToItem(0)
        // Si prefieres sin animación: listState.scrollToItem(0)
    }

    val paragraphProvider = remember(books) {
        {
            if (books.isEmpty()) {
                listOf("No hay libros disponibles por ahora.")
            } else {
                books.map { b ->
                    val title = b.title.ifBlank { "Título desconocido" }
                    val author = b.author.ifBlank { "Autor desconocido" }
                    val pub = b.publisher.ifBlank { "Editorial desconocida" }
                    "$title, de $author. Editorial $pub."
                }
            }
        }
    }

    ScreenScaffold(
        title = "HOME",
        canNavigateBack = false,
        onBack = null,
        paragraphProvider = paragraphProvider,
        topBarColor = MaterialTheme.colorScheme.primary,
        topBarIconSize = 28.dp
        // 👇 Ya NO pasamos onDecFont/onIncFont al TopBar
    ) {

        if (books.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .semantics { contentDescription = "No hay libros disponibles" },
                contentAlignment = Alignment.Center
            ) {
                Text("No hay libros disponibles por ahora.", style = MaterialTheme.typography.titleMedium)
            }
            return@ScreenScaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = "Listado de libros" },
            state = listState,
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ——— Controles A− / A+ (debajo del TopBar) ———
            item {
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
            }

            // ——— Bienvenida ———
            item {
                val baseSize: TextUnit = MaterialTheme.typography.titleMedium.fontSize
                val scaledSize: TextUnit = (baseSize.value * fontScale).sp
                val finalSize: TextUnit = if (scaledSize < 20.sp) 20.sp else scaledSize  // mínimo 20sp

                Text(
                    text = "Bienvenido 👋\nExplora nuestros resúmenes y descubre tu próxima lectura favorita.",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = finalSize),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)  // padding lateral extra
                        .padding(bottom = 12.dp)
                )
                Spacer(Modifier.height(8.dp))
            }

            // ——— Cards de libros ———
            items(items = books, key = { it.id }) { book ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Screen.BookDetail.createRoute(book.id)) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 👇 AQUÍ reemplazas tu AsyncImage anterior por este bloque:
                        val model: Any =
                            if (book.imageUrl.isNotBlank()) book.imageUrl
                            else R.drawable.ic_book_placeholder

                        AsyncImage(
                            model = model,
                            contentDescription = "Portada de ${book.title.ifBlank { "Libro" }}",
                            modifier = Modifier
                                .size(86.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.ic_book_placeholder),
                            error = painterResource(R.drawable.ic_book_placeholder)
                        )

                        Spacer(Modifier.width(14.dp))

                        Column(Modifier.weight(1f)) {
                            Text(
                                text = book.title.ifBlank { "Título desconocido" },
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = (MaterialTheme.typography.titleLarge.fontSize.value * fontScale)
                                        .coerceAtLeast(18f) // mínimo 18sp
                                        .sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = book.author.ifBlank { "Autor desconocido" },
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = (MaterialTheme.typography.bodyLarge.fontSize.value * fontScale)
                                        .coerceAtLeast(14f) // mínimo 14sp
                                        .sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                            )

                            Text(
                                text = book.publisher.ifBlank { "Editorial desconocida" },
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = (MaterialTheme.typography.bodyMedium.fontSize.value * fontScale)
                                        .coerceAtLeast(12f) // mínimo 12sp
                                        .sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}
