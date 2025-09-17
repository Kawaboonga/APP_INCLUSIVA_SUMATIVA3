package com.example.ref01.data


object BookRepository {
    private val books = listOf(
        Book(1,"El Alquimista","Paulo Coelho","Planeta","https://picsum.photos/seed/1/200",
            "Un viaje espiritual que invita a seguir los sueños.\n\nLa historia de Santiago inspira a escuchar el corazón."),
        Book(2,"Cien Años de Soledad","García Márquez","Sudamericana","https://picsum.photos/seed/2/200",
            "La saga de los Buendía en Macondo.\n\nRealismo mágico y memoria colectiva."),
        Book(3,"1984","George Orwell","Secker & Warburg","https://picsum.photos/seed/3/200",
            "Distopía sobre vigilancia y control.\n\nUn llamado a proteger la libertad."),
        Book(4,"Fahrenheit 451","Ray Bradbury","Ballantine","https://picsum.photos/seed/4/200",
            "La censura y el valor de los libros.\n\nQuemar para callar, leer para pensar."),
        Book(5,"El Principito","A. de Saint-Exupéry","Reynal & Hitchcock","https://picsum.photos/seed/5/200",
            "La mirada de un niño revela lo esencial.\n\nAmistad, amor y responsabilidad."),
        Book(6,"Sapiens","Yuval N. Harari","Harvill Secker","https://picsum.photos/seed/6/200",
            "Historia de la humanidad en claves simples.\n\nCómo relatos nos unifican."),
        Book(7,"Hábitos Atómicos","James Clear","Avery","https://picsum.photos/seed/7/200",
            "Cambios pequeños, resultados extraordinarios.\n\nSistema > Objetivo."),
        Book(8,"Deep Work","Cal Newport","Grand Central","https://picsum.photos/seed/8/200",
            "Concentración intensa para producir valor.\n\nReducir distracciones, elevar calidad."),
        Book(9,"Mindset","Carol Dweck","Random House","https://picsum.photos/seed/9/200",
            "Mentalidad fija vs crecimiento.\n\nEl esfuerzo desarrolla talento."),
        Book(10,"Drive","Daniel Pink","Riverhead","https://picsum.photos/seed/10/200",
            "Motivación: autonomía, maestría y propósito.\n\nEmpujar por lo que importa.")
    )

    fun getAll(): List<Book> = books
    fun getById(id: Int): Book? = books.firstOrNull { it.id == id }
}
