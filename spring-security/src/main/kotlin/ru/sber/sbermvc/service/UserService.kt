package ru.sber.sbermvc.service

import ru.sber.sbermvc.entity.Role
import ru.sber.sbermvc.entity.User

interface UserService {
    fun save(user: User)

    fun addRole(user: User, role: Role)

    fun findByUsername(username: String): User?
}