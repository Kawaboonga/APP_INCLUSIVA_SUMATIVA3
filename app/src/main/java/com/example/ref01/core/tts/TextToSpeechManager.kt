package com.example.ref01.core.tts

import android.content.Context
import android.media.AudioAttributes
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale
import java.util.UUID

/**
 * Gestor de Text-to-Speech (Google TTS).
 * Lee un texto o una lista de párrafos, con controles siguiente/anterior/pausa.
 */
class TextToSpeechManager(
    private val context: Context,
    private val onReady: () -> Unit = {},
    private val onUtteranceDone: (index: Int) -> Unit = {},
    private val onError: ((String) -> Unit)? = null
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var queue: List<String> = emptyList()
    private var currentIndex = 0
    private var isReady = false

    // Config
    var speechRate = 1.0f
    var pitch = 1.0f

    init { tts = TextToSpeech(context, this) }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) {
            onError?.invoke("TextToSpeech no inicializó (status=$status)")
            return
        }

        // Selección robusta de idioma
        val candidates = listOf(
            Locale("es", "CL"),
            Locale("es", "ES"),
            Locale("es"),
            Locale.ENGLISH
        )
        var languageOk = false
        for (loc in candidates) {
            val res = tts?.setLanguage(loc)
            if (res == TextToSpeech.LANG_AVAILABLE ||
                res == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
                res == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE
            ) {
                languageOk = true
                Log.d("TTS", "Idioma TTS seleccionado: $loc")
                break
            }
        }
        if (!languageOk) {
            onError?.invoke("No hay idioma TTS compatible instalado. Activa/instala 'Servicios de voz de Google'.")
            return
        }

        // Canal de accesibilidad (mezcla mejor con TalkBack/media)
        tts?.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
        )

        // Avance automático de párrafos
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) { /* no-op */ }

            override fun onError(utteranceId: String?) {
                onError?.invoke("Error de síntesis en $utteranceId")
            }

            override fun onDone(utteranceId: String?) {
                // IDs con formato: "utt#<index>#<uuid>"
                val idx = utteranceId
                    ?.split("#")
                    ?.getOrNull(1)
                    ?.toIntOrNull() ?: return

                onUtteranceDone(idx)
                if (idx + 1 < queue.size) speakIndex(idx + 1, flush = false)
            }
        })

        isReady = true
        onReady()
    }

    /** Lee un único texto (reemplaza la cola). */
    fun speak(text: String) {
        if (!isReady || text.isBlank()) return
        queue = listOf(text)
        currentIndex = 0
        tts?.setSpeechRate(speechRate)
        tts?.setPitch(pitch)

        val id = "utt#0#${UUID.randomUUID()}"
        // Importante: null en lugar de Bundle()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, id)
    }

    /** Lee una lista de párrafos en orden. */
    fun speakParagraphs(paragraphs: List<String>, startIndex: Int = 0) {
        if (!isReady || paragraphs.isEmpty()) return

        // Limpieza mínima: quita guiones iniciales y espacios
        val cleaned = paragraphs.mapNotNull { p ->
            var q = p.trim()
            while (q.startsWith("-")) q = q.drop(1).trim()
            q.takeIf { it.isNotBlank() }
        }
        if (cleaned.isEmpty()) return

        queue = cleaned
        currentIndex = startIndex.coerceIn(0, queue.lastIndex)
        tts?.setSpeechRate(speechRate)
        tts?.setPitch(pitch)

        speakIndex(currentIndex, flush = true)
    }

    fun pause() { tts?.stop() }  // Pausa (stop) → resume vuelve a leer desde el inicio del párrafo
    fun resume() { if (queue.isNotEmpty()) speakIndex(currentIndex, flush = true) }
    fun stopAll() { tts?.stop(); queue = emptyList(); currentIndex = 0 }
    fun previous() { if (queue.isNotEmpty()) { currentIndex = (currentIndex - 1).coerceAtLeast(0); speakIndex(currentIndex, true) } }
    fun next() { if (queue.isNotEmpty()) { currentIndex = (currentIndex + 1).coerceAtMost(queue.lastIndex); speakIndex(currentIndex, true) } }
    fun release() { tts?.shutdown() }

    private fun speakIndex(index: Int, flush: Boolean = false) {
        currentIndex = index
        val id = "utt#$index#${UUID.randomUUID()}"
        val mode = if (flush) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
        // Importante: null en lugar de Bundle()
        tts?.speak(queue[index], mode, null, id)
    }
}
