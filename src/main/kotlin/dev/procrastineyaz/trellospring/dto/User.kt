package dev.procrastineyaz.trellospring.dto

import dev.procrastineyaz.trellospring.models.User

data class NewUserDto(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String
)

data class SafeUserDataDto(
    val id: String?,
    val username: String,
    val firstName: String,
    val lastName: String
) {
    companion object {
        fun from(user: User) = SafeUserDataDto(user.id, user.username, user.firstName, user.lastName)
    }
}