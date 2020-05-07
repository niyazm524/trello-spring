package dev.procrastineyaz.trellospring.security.extensions

import dev.procrastineyaz.trellospring.security.jwt.UserDetailsImpl
import org.springframework.security.core.Authentication


val Authentication.userId
get() = (this.principal as UserDetailsImpl).userId
