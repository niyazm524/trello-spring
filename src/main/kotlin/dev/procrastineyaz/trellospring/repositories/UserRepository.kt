package dev.procrastineyaz.trellospring.repositories

import dev.procrastineyaz.trellospring.models.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserRepository : ReactiveMongoRepository<User, String> {
    fun findByUsernameAndEmail(username: String, email: String): Mono<User>
    fun findByUsernameOrEmail(username: String, email: String): Mono<User>
}