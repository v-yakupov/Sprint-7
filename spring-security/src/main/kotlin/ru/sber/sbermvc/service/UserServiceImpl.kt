package ru.sber.sbermvc.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Service
import ru.sber.sbermvc.entity.Role
import ru.sber.sbermvc.entity.User
import ru.sber.sbermvc.repository.UserRepository

@Service
class UserServiceImpl : UserService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var argon2PasswordEncoder: Argon2PasswordEncoder

    override fun save(user: User) {
        user.password = argon2PasswordEncoder.encode(user.password)
        userRepository.save(user)
    }

    override fun addRole(user: User, role: Role) {
        user.roles.add(role)
        userRepository.save(user)
    }

    override fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }
}