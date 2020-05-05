package dev.procrastineyaz.trellospring.security.controllers

import dev.procrastineyaz.trellospring.security.jwt.JWTReactiveAuthenticationManager
import dev.procrastineyaz.trellospring.security.jwt.TokenProvider
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono


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
                println(authenticationToken.toString())
                val auth = authenticationManager.authenticate(authenticationToken)
                ReactiveSecurityContextHolder.withAuthentication(authenticationToken)
                auth
            }
            .map { auth: Authentication ->
                val token = tokenProvider.createToken(auth)
                println("Generated token: $token")
                JWTToken(token)
            }
            .onErrorMap { ResponseStatusException(HttpStatus.BAD_REQUEST, "malformed credentials") }
    }

}

data class LoginVM(val username: String, val password: String)

data class JWTToken(val token: String)