package com.example.ref01.data.local

import android.content.Context
import androidx.core.content.contentValuesOf
import com.example.ref01.data.User

class UserDao(private val ctx: Context) {
    private val db get() = DBHelper(ctx).writableDatabase

    // === REGISTRO ===
    fun register(
        usernameInput: String,
        passwordInput: String,
        firstName: String?,
        lastName: String?,
        emailInput: String,
        country: String?,
        rut: String? = null
    ): Result<Unit> = runCatching {
        val username = usernameInput.trim()
        val password = passwordInput.trim()
        val email = emailInput.trim()

        // Validaciones
        if (userExists(username)) {
            throw IllegalStateException("El nombre de usuario ya está en uso.")
        }
        if (emailExists(email)) {
            throw IllegalStateException("El correo ya está en uso.")
        }

        val values = contentValuesOf(
            "username" to username,
            "password" to password,
            "first_name" to firstName?.trim(),
            "last_name" to lastName?.trim(),
            "email" to email,
            "country" to country?.trim(),
            "rut" to rut?.trim()
        )
        db.insertOrThrow("users", null, values)
    }

    // === LOGIN ===
    fun login(usernameInput: String, passwordInput: String): Result<Unit> = runCatching {
        val username = usernameInput.trim()
        val password = passwordInput.trim()
        db.rawQuery(
            "SELECT id FROM users WHERE username = ? COLLATE NOCASE AND password = ?",
            arrayOf(username, password)
        ).use { c ->
            if (!c.moveToFirst()) {
                throw IllegalArgumentException("Usuario o contraseña incorrectos")
            }
        }
    }

    // === EXISTE USUARIO ===
    fun userExists(usernameInput: String): Boolean {
        val username = usernameInput.trim()
        return db.rawQuery(
            "SELECT 1 FROM users WHERE username = ? COLLATE NOCASE LIMIT 1",
            arrayOf(username)
        ).use { it.moveToFirst() }
    }

    // === EXISTE EMAIL ===
    fun emailExists(emailInput: String): Boolean {
        val email = emailInput.trim()
        return db.rawQuery(
            "SELECT 1 FROM users WHERE email = ? LIMIT 1",
            arrayOf(email)
        ).use { it.moveToFirst() }
    }

    // === BUSCAR EMAIL POR USUARIO ===
    fun findEmail(usernameInput: String): String? {
        val username = usernameInput.trim()
        return db.rawQuery(
            "SELECT email FROM users WHERE username = ? COLLATE NOCASE",
            arrayOf(username)
        ).use { c -> if (c.moveToFirst()) c.getString(0) else null }
    }

    // === BUSCAR USUARIO COMPLETO ===
    fun findByUsername(usernameInput: String): User? {
        val username = usernameInput.trim()
        return db.rawQuery(
            """
            SELECT username, password, first_name, last_name, email, country, rut
            FROM users
            WHERE username = ? COLLATE NOCASE
            """.trimIndent(),
            arrayOf(username)
        ).use { c ->
            if (!c.moveToFirst()) return null
            User(
                username = c.getString(0),
                password = c.getString(1),
                firstName = c.getString(2) ?: "",
                lastName  = c.getString(3) ?: "",
                email     = c.getString(4) ?: "",
                country   = c.getString(5) ?: "",
                rut       = c.getString(6)
            )
        }
    }

    // === ACTUALIZAR DATOS DE USUARIO ===
    fun updateUser(u: User): Result<Unit> = runCatching {
        val values = contentValuesOf(
            "first_name" to u.firstName.trim(),
            "last_name"  to u.lastName.trim(),
            "email"      to u.email.trim(),
            "rut"        to (u.rut?.trim()),
            "country"    to u.country.trim()
        )
        val rows = db.update(
            "users",
            values,
            "username = ? COLLATE NOCASE",
            arrayOf(u.username.trim())
        )
        if (rows <= 0) throw IllegalStateException("No se pudo actualizar el usuario")
    }
}
