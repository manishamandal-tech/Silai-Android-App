package com.silai.app.models

/**
 * Models.kt
 * ============
 * Data classes represent the structure of our database tables.
 * In Kotlin, data classes auto-generate: equals(), hashCode(), toString(), copy()
 *
 * VIVA TIP: Data classes are perfect for model objects — they're immutable by default
 * and Kotlin generates all boilerplate automatically.
 */

// Represents a row in the 'users' table
data class User(
    val id: Int = 0,
    val username: String,
    val password: String,
    val email: String = "",
    val phone: String = ""
)

// Represents a row in the 'tailors' table
data class Tailor(
    val id: Int = 0,
    val name: String,
    val area: String,
    val rating: Double = 4.0,
    val imageUrl: String = "",
    val address: String = "",
    val description: String = "",
    val phone: String = ""
)

// Represents a row in the 'orders' table
data class Order(
    val id: Int = 0,
    val userId: Int,
    val tailorId: Int,
    val tailorName: String,
    val clothType: String,
    val date: String,
    val time: String,
    val status: String,
    val instructions: String = "",
    val imagePath: String = ""
)