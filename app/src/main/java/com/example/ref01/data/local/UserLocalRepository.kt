package com.example.ref01.data.local

import android.content.Context
import androidx.core.content.edit
import com.example.ref01.data.User

/**
 * Repo de usuarios basado en SQLite + SharedPreferences (para sesión)
 *
 * - Mantiene demo login gracias al seed del DBHelper.
 * - Guarda "usuario actual" en SharedPreferences.
 */
class UserLocalRepository(private val ctx: Context) {
    private val dao = UserDao(ctx)
    private val prefs = ctx.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun register(
        username: String,
        password: String,
        firstName: String?,
        lastName: String?,
        email: String,           // no nulo (UNIQUE en DB)
        country: String?
    ): Result<Unit> = dao.register(
        usernameInput = username,
        passwordInput = password,
        firstName = firstName,
        lastName = lastName,
        emailInput = email,
        country = country
    )

    fun login(username: String, password: String): Result<Unit> {
        return dao.login(username, password).onSuccess {
            // Guardamos usuario en sesión (trim para evitar espacios)
            prefs.edit { putString(KEY_USER, username.trim()) }
        }
    }

    fun logout() {
        prefs.edit { remove(KEY_USER) }
    }

    fun currentUser(): String? = prefs.getString(KEY_USER, null)

    fun recoverPassword(username: String): Result<String> = runCatching {
        dao.findEmail(username) ?: throw IllegalArgumentException("Usuario no encontrado")
    }

    /** Detalles completos del usuario actual (o null si no hay sesión) */
    fun currentUserDetails(): User? {
        val u = currentUser() ?: return null
        return dao.findByUsername(u)
    }

    /** Actualiza datos del usuario actualmente logueado (no cambia username ni password aquí). */
    fun updateCurrentUser(
        firstName: String,
        lastName: String,
        email: String,
        rut: String?,
        country: String
    ): Result<Unit> {
        val username = currentUser() ?: return Result.failure(IllegalStateException("Sin sesión"))
        val user = User(
            username = username,
            password = "", // no cambia aquí
            firstName = firstName,
            lastName = lastName,
            email = email,
            country = country,
            rut = rut
        )
        return dao.updateUser(user)
    }

    companion object {
        private const val KEY_USER = "current_user"
    }
}
