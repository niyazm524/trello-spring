package dev.procrastineyaz.trellospring.repositories

import dev.procrastineyaz.trellospring.models.BoardList
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface BoardListRepository : ReactiveMongoRepository<BoardList, String> {
}