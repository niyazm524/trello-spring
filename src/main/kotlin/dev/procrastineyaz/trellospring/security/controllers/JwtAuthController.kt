package dev.procrastineyaz.trellospring.security.controllers

import dev.procrastineyaz.trellospring.security.jwt.JWTReactiveAuthenticationManager
import dev.procrastineyaz.trellospring.security.jwt.TokenProvider
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty


@RestController
@RequestMapping("/api/login")
class JwtAuthController(
    private val tokenProvider: TokenProvider,
    private val authenticationManager: JWTReactiveAuthenticationManager
) {

    @PostMapping
    fun authorize(@RequestBody loginVM: Mono<LoginVM>): Mono<JWTToken> {
        return loginVM.filter { it.username.isNotEmpty() && it.password.isNotEmpty() }
            .flatMap { loginVm ->
                val authenticationToken: Authentication = UsernamePasswordAuthenticationToken(loginVm.username, loginVm.password)
                authenticationManager.authenticate(authenticationToken)
            }
            .doOnNext { ReactiveSecurityContextHolder.withAuthentication(it) }
            .filter { it.isAuthenticated }
            .switchIfEmpty { Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or password is incorrect")) }
            .map { auth: Authentication -> JWTToken(token = tokenProvider.createToken(auth)) }
    }

}

data class LoginVM(val username: String, val password: String)

data class JWTToken(val token: String)