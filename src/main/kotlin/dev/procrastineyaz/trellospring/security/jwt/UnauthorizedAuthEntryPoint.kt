package dev.procrastineyaz.trellospring.security.jwt

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class UnauthorizedAuthEntryPoint : ServerAuthenticationEntryPoint {
    override fun commence(exchange: ServerWebExchange, e: AuthenticationException): Mono<Void> {
        return Mono.fromRunnable {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
        }
    }
}