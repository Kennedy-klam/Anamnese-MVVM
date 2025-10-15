package com.kennedy.cadastrofirestore.data

data class User(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val healthHistory: HealthHistory = HealthHistory()
)