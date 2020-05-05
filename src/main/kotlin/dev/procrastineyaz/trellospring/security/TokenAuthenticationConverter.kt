package dev.procrastineyaz.trellospring.security

import dev.procrastineyaz.trellospring.security.extensions.getTokenFromRequest
import dev.procrastineyaz.trellospring.security.jwt.TokenProvider
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.*
import java.util.function.Function


class TokenAuthenticationConverter(private val tokenProvider: TokenProvider) : ServerAuthenticationConverter {
    override fun convert(serverWebExchange: ServerWebExchange): Mono<Authentication> {
        return Mono.justOrEmpty(serverWebExchange)
            .map { it.getTokenFromRequest() }
            .filter { it.isNotEmpty() }
            .map { it.removePrefix(BEARER) }
            .filter { token -> token.isNotEmpty() }
            .map { token -> tokenProvider.getAuthentication(token) }
            .onErrorMap { t -> ResponseStatusException(HttpStatus.UNAUTHORIZED, t.message) }

    }

    companion object {
        private const val BEARER: String = "Bearer "
    }

}