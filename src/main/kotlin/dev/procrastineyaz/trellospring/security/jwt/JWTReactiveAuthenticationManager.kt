package dev.procrastineyaz.trellospring.security.jwt

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers


class JWTReactiveAuthenticationManager(
    private val userDetailsService: ReactiveUserDetailsService,
    private val passwordEncoder: PasswordEncoder
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return if (authentication.isAuthenticated) {
            Mono.just(authentication)
        } else Mono.just(authentication)
            .cast(UsernamePasswordAuthenticationToken::class.java)
            .flatMap { authenticationToken: UsernamePasswordAuthenticationToken -> authenticateToken(authenticationToken) }
            .cast(UserDetailsImpl::class.java)
            .publishOn(Schedulers.parallel())
            .filter { u: UserDetails -> passwordEncoder.matches(authentication.credentials as String, u.password) }
            .map { u: UserDetailsImpl -> JwtAuthentication(u).apply { isAuthenticated = true } }
            .cast(Authentication::class.java)
            .switchIfEmpty(Mono.just(authentication))
            .onErrorReturn(authentication)

    }

    private fun authenticateToken(authenticationToken: UsernamePasswordAuthenticationToken): Mono<UserDetails> {
        val username = authenticationToken.name
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            return userDetailsService.findByUsername(username)
        }
        return Mono.error(IllegalStateException("bad token"))
    }

}
