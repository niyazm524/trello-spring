package dev.procrastineyaz.trellospring.security.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class UserDetailsImpl(
        val userId: String, private val role: String, private val userName: String, private val pass: String? = null
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return SimpleGrantedAuthority(role).let { Collections.singleton(it) }
    }

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = userName

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String? {
        return pass
    }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}