package dev.procrastineyaz.trellospring.repositories

import dev.procrastineyaz.trellospring.models.Board
import dev.procrastineyaz.trellospring.models.User
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface BoardRepository : ReactiveMongoRepository<Board, String> {
    @Query(fields = "{ 'lists': 0 }")
    fun findByMembersIn(user: User): Flux<Board>
    fun findByIdAndMembersIn(id: String, user: User): Mono<Board>
    fun findByIdAndAuthor(id: String, author: User): Mono<Board>
    fun deleteByIdAndAuthor(id: String, author: User): Mono<Void>
}