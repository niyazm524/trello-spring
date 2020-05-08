package dev.procrastineyaz.trellospring.security.extensions

import dev.procrastineyaz.trellospring.security.jwt.UserDetailsImpl
import org.springframework.security.core.Authentication


val Authentication.user
get() = (this.principal as UserDetailsImpl).user
