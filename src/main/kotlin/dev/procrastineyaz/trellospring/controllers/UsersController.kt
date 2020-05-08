package dev.procrastineyaz.trellospring.controllers

import dev.procrastineyaz.trellospring.dto.NewUserDto
import dev.procrastineyaz.trellospring.extensions.toUserModel
import dev.procrastineyaz.trellospring.repositories.UserRepository
import dev.procrastineyaz.trellospring.security.extensions.user
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/users")
class UsersController(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) {

    @GetMapping("/self")
    fun getSelf(auth: Authentication) = Mono.just(auth.user)

    @PostMapping
    fun createUser(@RequestBody newUser: Mono<NewUserDto>) =
        newUser.map { user -> user.toUserModel(password = passwordEncoder.encode(user.password)) }
            .flatMap { userRepository.save(it) }
}