package dev.procrastineyaz.trellospring.security.jwt

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.lang.Error


class JWTReactiveAuthenticationManager(
    private val userDetailsService: ReactiveUserDetailsService,
    private val passwordEncoder: PasswordEncoder
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return if (authentication.isAuthenticated) {
            Mono.just(authentication)
        } else Mono.just(authentication)
            .switchIfEmpty(Mono.defer { raiseBadCredentials<Authentication>() })
            .cast(UsernamePasswordAuthenticationToken::class.java)
            .flatMap { authenticationToken: UsernamePasswordAuthenticationToken -> authenticateToken(authenticationToken) }
            .publishOn(Schedulers.parallel())
            .onErrorResume { e: Throwable? -> raiseBadCredentials() }
            .filter { u: UserDetails -> passwordEncoder.matches(authentication.credentials as String, u.password) }
            .switchIfEmpty(Mono.defer { raiseBadCredentials<UserDetails>() })
            .map { u: UserDetails -> UsernamePasswordAuthenticationToken(authentication.principal, authentication.credentials, u.authorities) }
    }

    private fun <T> raiseBadCredentials(): Mono<T> {
        return Mono.error(BadCredentialsException("Invalid Credentials"))
    }

    private fun authenticateToken(authenticationToken: UsernamePasswordAuthenticationToken): Mono<UserDetails> {
        val username = authenticationToken.name
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            return userDetailsService.findByUsername(username)
        }
        return Mono.error(IllegalStateException("bad token"))
    }

}
