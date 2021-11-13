package ru.sber.sbermvc.service

import ru.sber.sbermvc.entity.Role

interface RoleService {
    fun save(role: Role)

    fun findByName(name: String): Role?
}