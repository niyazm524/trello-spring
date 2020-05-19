package dev.procrastineyaz.trellospring.dto

data class NewBoardDto(val title: String)
data class UpdatedBoardDto(val title: String?, val description: String?)
data class CardOrderDto(val newIndex: Int)
data class CardMoveDto(val destListId: String, val newIndex: Int)