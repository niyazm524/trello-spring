package dev.procrastineyaz.trellospring.service

import dev.procrastineyaz.trellospring.email.EmailTokenProvider
import dev.procrastineyaz.trellospring.models.User
import dev.procrastineyaz.trellospring.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class EmailService(
    private val javaMailSender: JavaMailSender,
    private val emailTokenProvider: EmailTokenProvider,
    private val userRepository: UserRepository
) {
    fun sendRegistrationEmail(user: User) {
        val msg = javaMailSender.createMimeMessage()
        MimeMessageHelper(msg, false, "UTF-8").apply {
            addTo(user.email)
            setFrom("niyazm524@gmailcom")
            setSubject("Registration confirmation for user ${user.firstName} ${user.lastName}")
        }
        val token = emailTokenProvider.createToken(user)
        msg.setContent("""
<h3>Please confirm your account by clicking on http://localhost:8080/enable-account/${token}</h3>
""".trimIndent(), "text/html")
        javaMailSender.send(msg)
    }

    fun confirmEmail(token: String): User {
        val claims = emailTokenProvider.getTokenClaims(token)
        val user = userRepository
            .findByUsernameAndEmail(claims["username"].toString(), claims.subject.toString()).block()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "This user can't be found")
        return userRepository.save(user.copy(isEnabled = true)).block() ?: error("user saving error")
    }
}
