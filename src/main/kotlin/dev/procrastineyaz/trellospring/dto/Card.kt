package dev.procrastineyaz.trellospring.dto

data class NewCardDto(val title: String)
data class UpdatedCardDto(val title: String?, val description: String?)