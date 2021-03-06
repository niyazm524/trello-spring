package dev.procrastineyaz.trellospring.security.jwt

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class JwtAuthentication(
    private val userDetailsImpl: UserDetailsImpl,
    private var isAuthenticated: Boolean = false
) : Authentication {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return userDetailsImpl.authorities
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
    }

    override fun getName(): String = userDetailsImpl.username

    override fun getCredentials(): Any? = null

    override fun getPrincipal(): Any? = userDetailsImpl.user.id

    override fun isAuthenticated(): Boolean = isAuthenticated

    override fun getDetails(): Any? = userDetailsImpl
}