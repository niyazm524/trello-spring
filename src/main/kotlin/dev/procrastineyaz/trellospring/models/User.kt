package dev.procrastineyaz.trellospring.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class User(
    @Id val id: String? = null,
    val email: String,
    @Indexed(unique = true) val username: String,
    val firstName: String,
    val lastName: String,
    @JsonIgnore val password: String,
    val role: UserRole
)

enum class UserRole {
    USER, ADMIN
}