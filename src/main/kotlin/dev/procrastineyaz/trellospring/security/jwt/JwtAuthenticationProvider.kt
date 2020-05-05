package dev.procrastineyaz.trellospring.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

//class JwtAuthenticationProvider : AuthenticationProvider {
//
//    @Value("secret")
//    private lateinit var secret: String
//
//    override fun authenticate(authentication: Authentication): Authentication {
//        val token = authentication.name
//        val claims: Claims = try {
//            Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
//        } catch (exception: JwtException) {
//            throw AuthenticationCredentialsNotFoundException("Bad token")
//        }
//
//        val userDetails = UserDetailsImpl(
//                userId = claims["sub"] as String,
//                name = claims["name"] as String,
//                role = claims["role"] as String
//        )
//
//        authentication.isAuthenticated = true
//        (authentication as JwtAuthentication).userDetails = userDetails
//        return authentication
//    }
//
//    override fun supports(authentication: Class<*>?): Boolean {
//        return JwtAuthentication::class.java == authentication
//    }
//}