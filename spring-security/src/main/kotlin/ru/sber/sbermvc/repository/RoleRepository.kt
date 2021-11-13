package ru.sber.sbermvc.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.sbermvc.entity.Role

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: String): Role?
}