package dev.procrastineyaz.trellospring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TrelloSpringApplication

fun main(args: Array<String>) {
	runApplication<TrelloSpringApplication>(*args)
}
