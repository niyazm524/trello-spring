package dev.procrastineyaz.trellospring.routes

import dev.procrastineyaz.trellospring.handlers.BoardsHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class MainRouter(private val boardsHandler: BoardsHandler) {
    @Bean
    fun route() = router {
        (accept(MediaType.APPLICATION_JSON) and "/api").nest {
            "/boards".nest {
                GET("/", boardsHandler::test)
            }
        }
    }
}