package com.example.ref01.data


data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val publisher: String,
    val imageUrl: String,
    val summary: String
) {
    fun paragraphs(): List<String> =
        summary.split("\n\n").map { it.trim() }.filter { it.isNotBlank() }
}
