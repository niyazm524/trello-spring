package dev.procrastineyaz.trellospring.security.jwt

import dev.procrastineyaz.trellospring.models.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class UserDetailsImpl(
        private val user: User
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return SimpleGrantedAuthority(user.role.toString()).let { Collections.singleton(it) }
    }

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = user.username

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String? {
        return user.password
    }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}