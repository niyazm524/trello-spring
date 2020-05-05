package dev.procrastineyaz.trellospring.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("lists")
data class BoardList(
        @Id val id: String? = null,
        val title: String,
        val cards: List<Card> = listOf()
)