package dev.procrastineyaz.trellospring.security

import dev.procrastineyaz.trellospring.models.User
import dev.procrastineyaz.trellospring.repositories.UserRepository
import dev.procrastineyaz.trellospring.security.extensions.getTokenFromRequest
import dev.procrastineyaz.trellospring.security.jwt.JwtAuthentication
import dev.procrastineyaz.trellospring.security.jwt.TokenProvider
import dev.procrastineyaz.trellospring.security.jwt.UserDetailsImpl
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


class TokenAuthenticationConverter(
    private val tokenProvider: TokenProvider,
    private val userRepository: UserRepository
) : ServerAuthenticationConverter {
    override fun convert(serverWebExchange: ServerWebExchange): Mono<Authentication> {
        return Mono.justOrEmpty(serverWebExchange)
            .map { it.getTokenFromRequest() }
            .filter { token -> token.isNotEmpty() }
            .map { token -> tokenProvider.getTokenClaims(token).subject }
            .flatMap { userId -> userRepository.findById(userId) }
            .filter { user -> user.isEnabled }
            .map<Authentication> { user: User -> JwtAuthentication(UserDetailsImpl(user), true) }
            .onErrorMap { t -> ResponseStatusException(HttpStatus.UNAUTHORIZED, t.message) }

    }

}