package dev.procrastineyaz.trellospring.security.jwt

import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class JWTHeadersExchangeMatcher : ServerWebExchangeMatcher {
    override fun matches(exchange: ServerWebExchange): Mono<ServerWebExchangeMatcher.MatchResult> {
        val request: Mono<ServerHttpRequest> = Mono.just(exchange).map { obj: ServerWebExchange -> obj.request }
        return request.map(ServerHttpRequest::getHeaders)
            .filter{ h -> h.containsKey(HttpHeaders.AUTHORIZATION) }
            .flatMap { ServerWebExchangeMatcher.MatchResult.match() }
            .switchIfEmpty(ServerWebExchangeMatcher.MatchResult.notMatch())
    }
}