package dev.procrastineyaz.trellospring.controllers

import dev.procrastineyaz.trellospring.dto.NewBoardDto
import dev.procrastineyaz.trellospring.dto.UpdatedBoardDto
import dev.procrastineyaz.trellospring.models.Board
import dev.procrastineyaz.trellospring.repositories.BoardRepository
import dev.procrastineyaz.trellospring.security.extensions.user
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/boards")
class BoardsController(private val boardRepository: BoardRepository) {

    @PostMapping
    fun createBoard(auth: Authentication, @RequestBody newBoard: Mono<NewBoardDto>): Mono<Board> =
        newBoard.map { Board(title = it.title, author = auth.user, members = listOf(auth.user)) }
            .flatMap { boardRepository.save(it) }

    @GetMapping
    fun getUserBoards(auth: Authentication): Flux<Board> =
        boardRepository.findByMembersIn(auth.user)

    @GetMapping("/{id}")
    fun getBoardById(auth: Authentication, @PathVariable("id") id: String): Mono<Board> =
        boardRepository.findByIdAndMembersIn(id, auth.user)

    @PutMapping("/{id}")
    fun updateBoardInfo(
        auth: Authentication,
        @PathVariable("id") id: String,
        @RequestBody updatedBoard: Mono<UpdatedBoardDto>
    ) = boardRepository.findByIdAndAuthor(id, auth.user)
        .zipWith(updatedBoard) { board, updated ->
            board.copy(title = updated.title ?: board.title, description = updated.description ?: board.description)
        }
        .flatMap { boardRepository.save(it) }

    @DeleteMapping("/{id}")
    fun deleteBoard(auth: Authentication, @PathVariable("id") id: String) =
        boardRepository.deleteByIdAndAuthor(id, auth.user)
}