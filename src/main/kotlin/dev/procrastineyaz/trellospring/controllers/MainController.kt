package dev.procrastineyaz.trellospring.controllers

import dev.procrastineyaz.trellospring.models.User
import dev.procrastineyaz.trellospring.models.UserRole
import dev.procrastineyaz.trellospring.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class MainController(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {
    @GetMapping("/api/users")
    fun getUsers(): Mono<String> = Mono.just("hello")

    @PostMapping("/api/users")
    fun createUser(@RequestBody newUser: Mono<NewUser>) =
        newUser.map { user -> User(
                username = user.username,
                password = passwordEncoder.encode(user.password),
                role = UserRole.USER
            ) }
            .flatMap { userRepository.save(it) }
}

data class NewUser(val username: String, val password: String)