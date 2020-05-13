package dev.procrastineyaz.trellospring.controllers

import dev.procrastineyaz.trellospring.dto.NewBoardListDto
import dev.procrastineyaz.trellospring.dto.NewCardDto
import dev.procrastineyaz.trellospring.models.Board
import dev.procrastineyaz.trellospring.models.BoardList
import dev.procrastineyaz.trellospring.models.Card
import dev.procrastineyaz.trellospring.repositories.BoardListRepository
import dev.procrastineyaz.trellospring.repositories.BoardRepository
import dev.procrastineyaz.trellospring.security.extensions.user
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/boards/{id}/lists")
class BoardListsController(
    private val boardRepository: BoardRepository,
    private val boardListRepository: BoardListRepository
) {
    @PostMapping
    fun addNewList(auth: Authentication, @PathVariable id: String, @RequestBody newList: Mono<NewBoardListDto>) = newList
        .map { BoardList(title = it.title) }
        .flatMap { boardListRepository.save(it) }
        .zipWith(boardRepository.findByIdAndMembersIn(id, auth.user)) { boardList, board ->
            board.copy(lists = board.lists + boardList)
        }
        .flatMap { boardRepository.save(it) }

    @DeleteMapping("/{listId}")
    fun removeList(
        auth: Authentication,
        @PathVariable id: String,
        @PathVariable listId: String
    ): Mono<Board> = boardListRepository.deleteById(listId)
        .then(boardRepository.findByIdAndMembersIn(id, auth.user))
        .map { board -> board.copy(lists = board.lists.filter { it.id != listId }) }
        .flatMap { boardRepository.save(it) }

    @PostMapping("/{listId}/cards")
    fun addCardToList(
        auth: Authentication,
        @PathVariable id: String,
        @PathVariable listId: String,
        @RequestBody newCard: Mono<NewCardDto>
    ): Mono<BoardList> = boardRepository.findById_AndMembersIn(id, auth.user)
        .map { board -> board.lists.find { it.id == listId } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND) }
        .zipWith(newCard) { list, newCard ->
            list.copy(cards = list.cards + Card(id = ObjectId.get().toHexString(), title = newCard.title))
        }
        .flatMap { boardListRepository.save(it) }


}