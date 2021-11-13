package ru.sber.sbermvc.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.sbermvc.entity.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}