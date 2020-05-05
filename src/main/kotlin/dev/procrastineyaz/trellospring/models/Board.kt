package dev.procrastineyaz.trellospring.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document("boards")
data class Board(
        @Id val id: String? = null,
        val title: String,
        val description: String? = null,
        @DBRef val author: User,
        @DBRef val members: List<User> = listOf(),
        @DBRef val lists: List<BoardList> = listOf()
)