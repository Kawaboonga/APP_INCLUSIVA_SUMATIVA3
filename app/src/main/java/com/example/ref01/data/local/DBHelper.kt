package com.example.ref01.data.local

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

/**
 * DBHelper (SQLiteOpenHelper) — Base de datos local
 * - users: login/registro/recuperar
 * - comments: comentarios por libro
 */
class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla de usuarios (username insensible a mayúsculas con índice UNIQUE NOCASE)
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS users(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username   TEXT NOT NULL,
                password   TEXT NOT NULL,
                first_name TEXT,
                last_name  TEXT,
                email      TEXT NOT NULL UNIQUE,
                rut        TEXT,
                country    TEXT
            );
            """.trimIndent()
        )

        // Único por username ignorando mayúsculas/minúsculas
        db.execSQL(
            """
            CREATE UNIQUE INDEX IF NOT EXISTS idx_users_username_nocase
            ON users(username COLLATE NOCASE);
            """.trimIndent()
        )

        // Tabla de comentarios
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS comments(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                book_id   INTEGER NOT NULL,
                username  TEXT    NOT NULL,
                content   TEXT    NOT NULL,
                created_at INTEGER NOT NULL
            );
            """.trimIndent()
        )

        seedDemoUser(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Migraciones incrementales y seguras
        if (oldVersion < 3) {
            // 1) Agregar columna 'rut' si no existe
            try {
                db.execSQL("ALTER TABLE users ADD COLUMN rut TEXT")
            } catch (_: SQLException) {
                // Ya existe; ignorar
            }
            // 2) Asegurar índice UNIQUE NOCASE para username
            db.execSQL(
                """
                CREATE UNIQUE INDEX IF NOT EXISTS idx_users_username_nocase
                ON users(username COLLATE NOCASE);
                """.trimIndent()
            )
        }
        // Si vienes de una BD muy vieja o corrupta, puedes forzar un reset:
        // db.execSQL("DROP TABLE IF EXISTS comments")
        // db.execSQL("DROP TABLE IF EXISTS users")
        // onCreate(db)
    }

    private fun seedDemoUser(db: SQLiteDatabase) {
        // Inserta demo solo si no existe
        val exists = db.rawQuery(
            "SELECT 1 FROM users WHERE username = ? COLLATE NOCASE LIMIT 1",
            arrayOf(DEFAULT_USERNAME)
        ).use { it.moveToFirst() }

        if (!exists) {
            val values = contentValuesOf(
                "username" to DEFAULT_USERNAME,
                "password" to DEFAULT_PASSWORD, // demo: plain text
                "first_name" to "Demo",
                "last_name" to "User",
                "email" to "demo@demo.com",
                "rut" to null,
                "country" to "Chile"
            )
            db.insert("users", null, values)
        }
    }

    companion object {
        const val DATABASE_NAME = "bookeasy.db"
        const val DATABASE_VERSION = 3  // <- ¡importante!

        // Códigos de error internos opcionales
        const val ERR_USER_EXISTS = 1001
        const val ERR_USER_NOT_FOUND = 1002
        const val ERR_INVALID_CREDENTIALS = 1003

        const val DEFAULT_USERNAME = "demo"
        const val DEFAULT_PASSWORD = "demo123"
    }
}
