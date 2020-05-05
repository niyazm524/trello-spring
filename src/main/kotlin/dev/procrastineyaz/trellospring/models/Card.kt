package dev.procrastineyaz.trellospring.models

import org.springframework.data.annotation.Id

data class Card(
        @Id val id: String? = null,
        val title: String,
        val description: String? = null
)