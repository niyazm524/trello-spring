package dev.procrastineyaz.trellospring.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Size

@Document("users")
data class User(
    @Id val id: String? = null,
    @Indexed(unique = true) val username: String,
    val password: String,
    val role: UserRole
)

enum class UserRole {
    USER, ADMIN
}