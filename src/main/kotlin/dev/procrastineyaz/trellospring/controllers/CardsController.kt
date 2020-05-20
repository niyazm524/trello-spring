package dev.procrastineyaz.trellospring.controllers

import dev.procrastineyaz.trellospring.dto.UpdatedCardDto
import dev.procrastineyaz.trellospring.repositories.BoardListRepository
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/boards/1/lists/{listId}/cards")
class CardsController(private val boardListRepository: BoardListRepository) {
    @PutMapping("/{cardId}")
    fun updateCard(auth: Authentication, @PathVariable listId: String, @PathVariable cardId: String, @RequestBody updatedCard: Mono<UpdatedCardDto>) =
        boardListRepository.findById(listId)
            .zipWith(updatedCard) { list, uCardDto ->
                val cards = list.cards.toMutableList()
                var card = cards.find { it.id == cardId } ?: error("not found")
                card = card.copy(title = uCardDto.title ?: card.title, description = uCardDto.description ?: card.description)
                cards[cards.indexOfFirst { it.id == cardId }] = card
                list.copy(cards = cards.toList())
            }
            .flatMap { boardListRepository.save(it) }
}