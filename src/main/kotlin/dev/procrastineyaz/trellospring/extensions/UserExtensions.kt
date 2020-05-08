package dev.procrastineyaz.trellospring.extensions

import dev.procrastineyaz.trellospring.dto.NewUserDto
import dev.procrastineyaz.trellospring.models.User
import dev.procrastineyaz.trellospring.models.UserRole

fun NewUserDto.toUserModel(password: String = this.password, role: UserRole = UserRole.USER) = User(
    username = this.username,
    password = password,
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email,
    role = role
)
