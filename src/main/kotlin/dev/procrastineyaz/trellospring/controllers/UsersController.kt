package dev.procrastineyaz.trellospring.controllers

import dev.procrastineyaz.trellospring.dto.NewUserDto
import dev.procrastineyaz.trellospring.dto.SafeUserDataDto
import dev.procrastineyaz.trellospring.extensions.toUserModel
import dev.procrastineyaz.trellospring.repositories.UserRepository
import dev.procrastineyaz.trellospring.security.extensions.user
import dev.procrastineyaz.trellospring.service.EmailService
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/users")
class UsersController(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val emailService: EmailService
) {

    @GetMapping("/self")
    fun getSelf(auth: Authentication) = Mono.just(auth.user)

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): Mono<SafeUserDataDto> =
        userRepository.findById(id).map { user -> SafeUserDataDto.from(user) }

    @PostMapping
    fun createUser(@RequestBody newUser: Mono<NewUserDto>) = newUser.map { user ->
        user.toUserModel(password = passwordEncoder.encode(user.password))
    }.flatMap {
        emailService.sendRegistrationEmail(it)
        userRepository.save(it)
    }
}
