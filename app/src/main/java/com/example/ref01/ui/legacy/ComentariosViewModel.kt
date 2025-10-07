package com.example.ref01.ui.legacy

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel simple y 100% testeable en JVM (sin dependencias Android).
 * Mantiene:
 * - bookId (para saber a qué libro pertenecen los comentarios)
 * - lista de comentarios (String)
 * - status (para que tests verifiquen transiciones)
 */
class ComentariosViewModel(
    private val clock: () -> Long = { System.currentTimeMillis() }
) : ViewModel() {

    var bookId: Int = 0
        private set

    private val _comments = MutableStateFlow<List<String>>(emptyList())
    val comments: StateFlow<List<String>> = _comments.asStateFlow()

    private val _status = MutableStateFlow("idle")
    val status: StateFlow<String> = _status.asStateFlow()

    fun setBook(id: Int) {
        bookId = id
        _status.value = "book_set"
    }

    fun addComment(text: String) {
        val t = text.trim()
        if (t.isEmpty()) {
            _status.value = "empty"
            return
        }
        _comments.value = _comments.value + t
        // “clock()” queda para extensiones futuras; lo dejamos disponible.
        _status.value = "added@${clock()}"
    }

    /** Elimina por índice. Devuelve true si pudo eliminar. */
    fun removeAt(index: Int): Boolean {
        val list = _comments.value
        if (index !in list.indices) {
            _status.value = "index_out_of_bounds"
            return false
        }
        _comments.value = list.toMutableList().also { it.removeAt(index) }
        _status.value = "removed"
        return true
    }

    /** Limpia todos los comentarios. */
    fun clear() {
        _comments.value = emptyList()
        _status.value = "cleared"
    }
}
