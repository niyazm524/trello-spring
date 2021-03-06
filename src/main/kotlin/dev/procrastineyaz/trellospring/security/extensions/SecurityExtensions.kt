package dev.procrastineyaz.trellospring.security.extensions

import org.springframework.http.HttpHeaders
import org.springframework.web.server.ServerWebExchange

fun ServerWebExchange.getTokenFromRequest(): String {
    val token: String = this.request
        .headers
        .getFirst(HttpHeaders.AUTHORIZATION) ?: ""
    return if (token.isEmpty()) "" else token.removePrefix("Bearer ")
}