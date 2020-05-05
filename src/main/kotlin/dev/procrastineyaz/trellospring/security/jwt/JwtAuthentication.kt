package dev.procrastineyaz.trellospring.security.jwt

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class JwtAuthentication(private val token: String) : Authentication {
    private var isAuthenticated: Boolean = false
    var userDetails: UserDetails? = null

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        TODO("Not yet implemented")
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
    }

    override fun getName(): String = token

    override fun getCredentials(): Any? = null

    override fun getPrincipal(): Any? = userDetails

    override fun isAuthenticated(): Boolean = isAuthenticated

    override fun getDetails(): Any? = null
}