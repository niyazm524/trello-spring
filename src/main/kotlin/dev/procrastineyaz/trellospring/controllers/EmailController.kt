package dev.procrastineyaz.trellospring.controllers

import dev.procrastineyaz.trellospring.service.EmailService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class EmailController(private val emailService: EmailService) {
    @GetMapping("/enable-account/{token}")

    fun enableAccount(@PathVariable token: String): String {
        val user = emailService.confirmEmail(token)
        return if(user.isEnabled) {
            "redirect:http://localhost:3000"
        } else {
            emailService.sendRegistrationEmail(user)
            "email-error"
        }
    }
}