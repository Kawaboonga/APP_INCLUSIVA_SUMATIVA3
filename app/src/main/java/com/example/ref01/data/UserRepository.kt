package com.example.ref01.data


object UserRepository {
    private val users: Array<User?> = arrayOfNulls(5)
    private var nextIndex: Int = 0

    fun addUser(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        email: String,
        country: String
    ): Result<User> {
        if (username.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank() || email.isBlank())
            return Result.failure(IllegalArgumentException("Completa todos los campos"))
        if (!email.contains("@") || !email.contains("."))
            return Result.failure(IllegalArgumentException("Correo inválido"))
        if (users.filterNotNull().any { it.username == username })
            return Result.failure(IllegalStateException("El usuario ya existe"))
        if (users.filterNotNull().any { it.email.equals(email, ignoreCase = true) })
            return Result.failure(IllegalStateException("El correo ya está registrado"))
        if (nextIndex >= users.size)
            return Result.failure(IllegalStateException("Capacidad completa (5 usuarios)"))

        val u = User(username, password, firstName, lastName, email, country)
        users[nextIndex] = u
        nextIndex++
        return Result.success(u)
    }

    fun validate(username: String, password: String): Boolean =
        users.filterNotNull().any { it.username == username && it.password == password }

    fun exists(username: String): Boolean =
        users.filterNotNull().any { it.username == username }

    fun list(): List<User> = users.filterNotNull()
}