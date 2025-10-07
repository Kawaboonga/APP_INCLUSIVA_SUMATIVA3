package com.example.ref01.ui.screens

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag // ← IMPORTANTE
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ref01.R
import com.example.ref01.data.BookRepository
import com.example.ref01.ui.components.PrimaryButton
import com.example.ref01.ui.components.ScreenScaffold
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.ref01.ui.legacy.ComentariosActivity

@Composable
fun BookDetailScreen(navController: NavController, bookId: Int) {
    val book = remember(bookId) { BookRepository.getById(bookId) }

    var fontScale by rememberSaveable { mutableFloatStateOf(1.0f) }
    val minScale = 0.9f
    val maxScale = 1.6f
    fun inc() { fontScale = (fontScale + 0.1f).coerceAtMost(maxScale) }
    fun dec() { fontScale = (fontScale - 0.1f).coerceAtLeast(minScale) }
    fun reset() { fontScale = 1.0f }

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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "No se encontró el libro solicitado.",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.testTag("book_not_found_text")
                    )
                    Spacer(Modifier.height(12.dp))
                    PrimaryButton(
                        text = "Volver",
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.testTag("back_button")
                    )
                }
            }
        }
        return
    }

    val ctx = LocalContext.current

    // Botón para abrir pantalla legacy de comentarios
    Button(
        onClick = {
            val i = Intent(ctx, ComentariosActivity::class.java).apply {
                putExtra(ComentariosActivity.EXTRA_BOOK_ID, bookId)
            }
            ctx.startActivity(i)
        },
        modifier = Modifier.testTag("open_legacy_comments_btn")
    ) {
        Text("Comentarios (Fragment/XML)")
    }

    val paragraphs = remember(book) { book.paragraphs() }
    val ttsParagraphs = remember(book) {
        buildList {
            add(book.title.ifBlank { "Título desconocido" })
            add("Autor: " + book.author.ifBlank { "Desconocido" })
            addAll(paragraphs)
        }
    }
    val context = LocalContext.current

    ScreenScaffold(
        title = "",
        canNavigateBack = true,
        onBack = { navController.popBackStack() },
        paragraphProvider = { ttsParagraphs },
        showTitle = false,
        showBackLabel = true,
        backLabel = "Volver",
        navController = navController
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
                        .padding(bottom = 12.dp)
                        .testTag("font_controls_row"),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(
                        onClick = { dec() },
                        modifier = Modifier.testTag("font_dec_btn")
                    ) { Text("A−") }
                    OutlinedButton(
                        onClick = { reset() },
                        modifier = Modifier.testTag("font_reset_btn")
                    ) { Text("A") }
                    FilledTonalButton(
                        onClick = { inc() },
                        modifier = Modifier.testTag("font_inc_btn")
                    ) { Text("A+") }
                }

                // Portada
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
                        .clip(RoundedCornerShape(16.dp))
                        .testTag("book_cover"),
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
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("book_title")
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Autor: " + book.author.ifBlank { "Desconocido" },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = min18sp(MaterialTheme.typography.bodyLarge.fontSize.value * fontScale)
                    ),
                    modifier = Modifier.testTag("book_author")
                )
                Text(
                    text = "Editorial: " + book.publisher.ifBlank { "Desconocida" },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = min18sp(MaterialTheme.typography.bodyMedium.fontSize.value * fontScale)
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.testTag("book_publisher")
                )

                Divider(Modifier.padding(vertical = 16.dp))

                if (paragraphs.isEmpty()) {
                    Text(
                        text = "No hay resumen disponible para este libro.",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = min18sp(MaterialTheme.typography.bodyLarge.fontSize.value * fontScale)
                        ),
                        modifier = Modifier.testTag("no_summary_text")
                    )
                } else {
                    paragraphs.forEachIndexed { idx, p ->
                        Text(
                            text = p,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = min18sp(MaterialTheme.typography.bodyLarge.fontSize.value * fontScale)
                            ),
                            modifier = Modifier.testTag("book_paragraph_$idx")
                        )
                        if (idx != paragraphs.lastIndex) Spacer(Modifier.height(12.dp))
                    }
                }

                Divider(Modifier.padding(vertical = 16.dp))

                // Comentarios
                CommentsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("comments_section"),
                    bookId = book.id
                )

                Spacer(Modifier.height(80.dp)) // espacio para el botón fijo
            }

            // Botón fijo inferior
            PrimaryButton(
                text = "Volver al Home",
                onClick = { navController.popBackStack("home", inclusive = false) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .testTag("go_home_btn")
            )
        }
    }
}

/* ----------------------- Comentarios (diálogo simple + tipografía grande) ----------------------- */

private data class CommentItem(val text: String, val timestamp: Long)

@Composable
private fun CommentsSection(
    modifier: Modifier = Modifier,
    bookId: Int
) {
    // Ajustes de legibilidad
    val commentFontSize = 20.sp
    val commentLineHeight = 28.sp
    val maxChars = 1000

    // Estado UI
    val comments = remember { mutableStateListOf<CommentItem>() }
    var text by rememberSaveable { mutableStateOf("") }
    var speechStatus by remember { mutableStateOf("Presiona el micrófono para dictar") }

    // Idioma del diálogo (cambia a "es-CL" si lo usas)
    val lang = remember { "es-ES" }

    // 1) Lanzador del diálogo (una sola vez, sin bucles)
    val speechDialogLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: androidx.activity.result.ActivityResult ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val data = result.data
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val best = matches?.firstOrNull()?.trim().orEmpty()
            if (best.isNotBlank()) {
                text = best.take(maxChars)
                speechStatus = "Transcrito"
            } else {
                speechStatus = "Sin resultado."
            }
        } else {
            speechStatus = "Cancelado."
        }
    }

    // 2) Permiso de micrófono
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            speechStatus = "Escuchando…"
            launchSpeechDialog(lang) { intent -> speechDialogLauncher.launch(intent) }
        } else {
            speechStatus = "Permiso de micrófono denegado"
        }
    }

    // 3) UI
    Column(modifier = modifier) {
        Text(
            "Comentarios",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.testTag("comments_title")
        )
        Spacer(Modifier.height(8.dp))

        // Campo de texto grande y cómodo
        OutlinedTextField(
            value = text,
            onValueChange = { input ->
                text = if (input.length <= maxChars) input else input.take(maxChars)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("comment_input"),
            singleLine = false,
            minLines = 3,
            maxLines = 6,
            textStyle = LocalTextStyle.current.copy(
                fontSize = commentFontSize,
                lineHeight = commentLineHeight
            ),
            placeholder = {
                Text(
                    "Escribe o usa el micrófono…",
                    fontSize = (commentFontSize * 0.95f),
                    lineHeight = commentLineHeight
                )
            },
            supportingText = {
                Text(
                    "${text.length} / $maxChars",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.testTag("comment_counter")
                )
            }
        )

        Spacer(Modifier.height(8.dp))

        // Fila de acciones
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("comment_actions_row"),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalButton(
                onClick = {
                    val trimmed = text.trim()
                    if (trimmed.isNotBlank()) {
                        comments += CommentItem(trimmed, System.currentTimeMillis())
                        text = ""
                        speechStatus = "Comentario publicado"
                    }
                },
                modifier = Modifier.testTag("publish_comment_btn")
            ) { Text("Publicar") }

            FilledTonalIconButton(
                onClick = {
                    // Pide permiso y abre el diálogo solo UNA vez
                    permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                },
                modifier = Modifier.testTag("mic_btn")
            ) {
                Icon(Icons.Filled.Mic, contentDescription = "Dictar comentario")
            }
        }

        Spacer(Modifier.height(6.dp))
        AssistiveText(speechStatus, modifier = Modifier.testTag("speech_status"))

        Spacer(Modifier.height(12.dp))
        Divider()
        Spacer(Modifier.height(12.dp))

        // Lista de comentarios
        if (comments.isEmpty()) {
            AssistiveText("Aún no hay comentarios. ¡Sé el primero!", modifier = Modifier.testTag("no_comments_text"))
        } else {
            comments.sortedByDescending { it.timestamp }.forEachIndexed { index, item ->
                CommentItemView(
                    item,
                    modifier = Modifier.testTag("comment_item_$index")
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

/** Helper: crea el intent del diálogo (sin reintentos ni bucles) */
private fun launchSpeechDialog(
    lang: String,
    launch: (Intent) -> Unit
) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, lang)
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora…")
    }
    launch(intent)
}

@Composable
private fun AssistiveText(msg: String, modifier: Modifier = Modifier) {
    Text(
        msg,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}

@Composable
private fun CommentItemView(item: CommentItem, modifier: Modifier = Modifier) {
    val commentFontSize = 20.sp
    val commentLineHeight = 28.sp

    Surface(
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            val time = remember(item.timestamp) {
                SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
                    .format(java.util.Date(item.timestamp))
            }
            Text(
                time,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 13.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.testTag("comment_time")
            )
            Spacer(Modifier.height(6.dp))
            Text(
                item.text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = commentFontSize,
                    lineHeight = commentLineHeight
                ),
                modifier = Modifier.testTag("comment_text")
            )
        }
    }
}
