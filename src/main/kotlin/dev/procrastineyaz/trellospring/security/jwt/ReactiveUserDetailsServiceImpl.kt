package dev.procrastineyaz.trellospring.security.jwt

import dev.procrastineyaz.trellospring.repositories.UserRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ReactiveUserDetailsServiceImpl(private val userRepository: UserRepository) : ReactiveUserDetailsService {
    override fun findByUsername(usernameOrEmail: String): Mono<UserDetails> {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .map { UserDetailsImpl(it) }
    }

}