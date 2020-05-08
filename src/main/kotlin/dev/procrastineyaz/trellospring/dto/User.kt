package dev.procrastineyaz.trellospring.dto

data class NewUserDto(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String
)
