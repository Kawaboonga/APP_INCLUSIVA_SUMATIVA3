package com.example.ref01.data.local

import android.content.Context


/**capa limpia para usar desde Compose o Fragment;
 * mantiene sesión con SharedPreferences.edit { } (KTX).**/

class CommentLocalRepository(ctx: Context) {
    private val dao = CommentDao(ctx)

    fun add(bookId: Int, username: String, content: String) = dao.addComment(bookId, username, content)

    fun list(bookId: Int) = dao.listForBook(bookId)
}
