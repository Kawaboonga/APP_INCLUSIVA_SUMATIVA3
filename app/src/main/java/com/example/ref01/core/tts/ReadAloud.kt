package com.example.ref01.core.tts

import android.app.Application
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.AndroidViewModel

interface ReadAloud {
    fun readText(text: String)
    fun readParagraphs(paragraphs: List<String>, startIndex: Int = 0)
    fun pause(); fun resume(); fun stop(); fun next(); fun previous()
    fun setRate(rate: Float); fun setPitch(pitch: Float)
}

class ReadAloudViewModel(app: Application) : AndroidViewModel(app), ReadAloud {
    private val mgr = TextToSpeechManager(
        app,
        onReady = { /* opcional */ },
        onUtteranceDone = { /* idx -> si quieres avanzar tú mismo */ }
    )

    override fun readText(text: String) = mgr.speak(text)
    override fun readParagraphs(paragraphs: List<String>, startIndex: Int) =
        mgr.speakParagraphs(paragraphs, startIndex)

    override fun pause() = mgr.pause()
    override fun resume() = mgr.resume()
    override fun stop() = mgr.stopAll()
    override fun next() = mgr.next()
    override fun previous() = mgr.previous()
    override fun setRate(rate: Float) { mgr.speechRate = rate }
    override fun setPitch(pitch: Float) { mgr.pitch = pitch }
    override fun onCleared() { super.onCleared(); mgr.release() }
}

val LocalReadAloud = staticCompositionLocalOf<ReadAloud> {
    error("ReadAloud no inicializado")
}
