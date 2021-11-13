package ru.sber.sbermvc.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.sber.sbermvc.repository.UserRepository

@Service
class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Override
    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
        if (user == null) throw UsernameNotFoundException(user)

        val grantedAuthorities = HashSet<GrantedAuthority>()

        user.roles.forEach { role ->
            println(role.name)
            grantedAuthorities.add(SimpleGrantedAuthority(role.name))
        }

        return User(user.username, user.password, grantedAuthorities)
    }
}