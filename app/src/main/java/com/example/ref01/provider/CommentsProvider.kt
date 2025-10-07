package com.example.ref01.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.ref01.data.local.CommentDao
import com.example.ref01.data.local.DBHelper

/**
 * CommentsProvider — read-only para exponer comentarios.
 * URIs:
 *   content://com.example.ref01.provider/comments            -> todos
 *   content://com.example.ref01.provider/comments/book/{id}  -> por libro
 *
 * Nota: insert/update/delete no soportados (solo lectura).
 */
class CommentsProvider : ContentProvider() {

    private lateinit var dao: CommentDao

    override fun onCreate(): Boolean {
        dao = CommentDao(requireNotNull(context))
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (URI_MATCHER.match(uri)) {
            CODE_ALL -> dao.queryAll()
            CODE_BY_BOOK -> {
                val bookId = uri.lastPathSegment?.toIntOrNull() ?: return null
                dao.queryByBook(bookId)
            }
            else -> null
        }
    }

    override fun getType(uri: Uri): String? = when (URI_MATCHER.match(uri)) {
        CODE_ALL -> "vnd.android.cursor.dir/vnd.${AUTHORITY}.comment"
        CODE_BY_BOOK -> "vnd.android.cursor.dir/vnd.${AUTHORITY}.comment"
        else -> null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0

    companion object {
        const val AUTHORITY = "com.example.ref01.provider"
        private const val CODE_ALL = 1
        private const val CODE_BY_BOOK = 2

        private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "comments", CODE_ALL)
            addURI(AUTHORITY, "comments/book/*", CODE_BY_BOOK)
        }
    }
}
