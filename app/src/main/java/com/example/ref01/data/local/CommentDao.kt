package com.example.ref01.data.local

import android.content.Context
import android.database.Cursor
import androidx.core.content.contentValuesOf

data class Comment(
    val id: Long,
    val bookId: Int,
    val username: String,
    val content: String,
    val createdAt: Long
)

class CommentDao(private val ctx: Context) {
    private val helper = DBHelper(ctx)
    private val db get() = helper.writableDatabase

    fun addComment(bookId: Int, username: String, content: String): Result<Long> = runCatching {
        val now = System.currentTimeMillis()
        val values = contentValuesOf(
            "book_id" to bookId,
            "username" to username,
            "content" to content,
            "created_at" to now
        )
        db.insert("comments", null, values)
    }

    fun listForBook(bookId: Int): List<Comment> {
        val items = mutableListOf<Comment>()
        db.rawQuery(
            "SELECT id, book_id, username, content, created_at FROM comments WHERE book_id=? ORDER BY created_at DESC",
            arrayOf(bookId.toString())
        ).use { c ->
            while (c.moveToNext()) {
                items.add(
                    Comment(
                        id = c.getLong(0),
                        bookId = c.getInt(1),
                        username = c.getString(2),
                        content = c.getString(3),
                        createdAt = c.getLong(4)
                    )
                )
            }
        }
        return items
    }

    // Expuesto para ContentProvider (consulta libre con proyección/filtros)
    fun queryAll(): Cursor =
        db.rawQuery("SELECT id, book_id, username, content, created_at FROM comments", emptyArray())

    fun queryByBook(bookId: Int): Cursor =
        db.rawQuery(
            "SELECT id, book_id, username, content, created_at FROM comments WHERE book_id=?",
            arrayOf(bookId.toString())
        )
}
