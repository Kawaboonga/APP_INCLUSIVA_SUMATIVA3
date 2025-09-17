package com.example.ref01.data

/** Repositorio DEMO **/
object UserRepository {

    // Capacidad máxima  5
    private const val MAX_USERS = 5

    // Arrays
    private val users: Array<User?> = arrayOfNulls(MAX_USERS)
    private var nextIndex: Int = 0

    // Usuario predeterminado
    const val DEFAULT_USERNAME = "demo"
    const val DEFAULT_PASSWORD = "demo123"
    private val defaultUser = User(
        username = DEFAULT_USERNAME,
        password = DEFAULT_PASSWORD,        // TEXTO PLANO DEMO
        firstName = "Usuario",
        lastName = "Demo",
        email = "demo@example.com",
        country = "Chile"
    )

    // Inicialización
    init {
        // Se siembra el usuario demo al inicio
        users[0] = defaultUser
        nextIndex = 1
    }


    // Agrega usuario
    fun addUser(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        email: String,
        country: String
    ): Result<User> {
        val u = username.trim()
        val f = firstName.trim()
        val l = lastName.trim()
        val e = email.trim()

        if (u.isBlank() || password.isBlank() || f.isBlank() || l.isBlank() || e.isBlank())
            return Result.failure(IllegalArgumentException("Completa todos los campos"))

        if (!isValidEmail(e))
            return Result.failure(IllegalArgumentException("Correo inválido"))

        // Duplicados
        if (list().any { it.username.equals(u, ignoreCase = true) })
            return Result.failure(IllegalStateException("El usuario ya existe"))

        if (list().any { it.email.equals(e, ignoreCase = true) })
            return Result.failure(IllegalStateException("El correo ya está registrado"))

        // Capacidad
        if (!hasCapacity())
            return Result.failure(IllegalStateException("Capacidad completa ($MAX_USERS usuarios)"))

        // Insertar
        val idx = firstFreeIndex() ?: return Result.failure(
            IllegalStateException("Capacidad completa ($MAX_USERS usuarios)")
        )
        val user = User(u, password, f, l, e, country)
        users[idx] = user
        // Mantenemos nextIndex
        nextIndex = (idx + 1).coerceAtMost(MAX_USERS)

        return Result.success(user)
    }

    // Valida credenciales para Login.
    fun validate(username: String, password: String): Boolean =
        list().any { it.username.equals(username, ignoreCase = true) && it.password == password }

    // Lista de usuarios guardados.
    fun list(): List<User> = users.filterNotNull()

    // Comprueba si un usuario existe por usernam
    fun exists(username: String): Boolean =
        list().any { it.username.equals(username, ignoreCase = true) }

    // Devuelve el usuario por username, o null si no existe.
    fun findByUsername(username: String): User? =
        list().firstOrNull { it.username.equals(username, ignoreCase = true) }

    // Helpers de capacidad
    fun count(): Int = list().size
    fun capacity(): Int = MAX_USERS
    fun hasCapacity(): Boolean = count() < capacity()
    fun remaining(): Int = capacity() - count()

    fun usernamesArray(): Array<String> = list().map { it.username }.toTypedArray()
    fun passwordsArray(): Array<String> = list().map { it.password }.toTypedArray()


    private fun isValidEmail(s: String): Boolean = s.contains("@") && s.contains(".")

    // Busca el primer índice libre en el array.
    private fun firstFreeIndex(): Int? {
        if (nextIndex in 0 until MAX_USERS && users[nextIndex] == null) return nextIndex
        // Si no, busca el primer null
        for (i in 0 until MAX_USERS) if (users[i] == null) return i  // bucle for
        return null
    }
}