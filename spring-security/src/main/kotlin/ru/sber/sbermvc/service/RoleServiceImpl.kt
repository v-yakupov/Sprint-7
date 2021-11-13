package ru.sber.sbermvc.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.sber.sbermvc.entity.Role
import ru.sber.sbermvc.repository.RoleRepository

@Service
class RoleServiceImpl : RoleService {
    @Autowired
    lateinit var roleRepository: RoleRepository

    override fun save(role: Role) {
        roleRepository.save(role)
    }

    override fun findByName(name: String): Role? {
        return roleRepository.findByName(name)
    }
}