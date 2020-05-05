package dev.procrastineyaz.trellospring.handlers

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class BoardsHandler {
    fun test(req: ServerRequest) = ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(mapOf("hello" to "world")), Map::class.java)
}