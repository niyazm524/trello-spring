package dev.procrastineyaz.trellospring.repositories

import dev.procrastineyaz.trellospring.models.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserRepository : ReactiveMongoRepository<User, String> {
    fun findByUsername(username: String): Mono<User>
}