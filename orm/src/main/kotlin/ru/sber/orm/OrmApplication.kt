package ru.sber.orm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrmApplication

fun main(args: Array<String>) {
    runApplication<OrmApplication>(*args)
}
