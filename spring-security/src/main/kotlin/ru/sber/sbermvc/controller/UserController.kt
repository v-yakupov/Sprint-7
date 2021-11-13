package ru.sber.sbermvc.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import ru.sber.sbermvc.service.RoleService
import ru.sber.sbermvc.service.UserService
import ru.sber.sbermvc.entity.Role
import ru.sber.sbermvc.entity.User
import javax.annotation.PostConstruct

@Controller
class UserController {
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var roleService: RoleService

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/register")
    fun register(): String {
        return "register"
    }

    @PostMapping("/register")
    fun registerUser(@ModelAttribute("user") user: User): String {
        userService.save(user)
        return "redirect:/app/list"
    }

    @PostConstruct
    fun fillUp() {
        userService.save(User(username = "admin", password = "admin"))
        userService.save(User(username = "api", password = "api"))
        userService.save(User(username = "user", password = "user"))
        roleService.save(Role(name = "ROLE_ADMIN"))
        roleService.save(Role(name = "ROLE_API"))
        userService.addRole(userService.findByUsername("admin")!!, roleService.findByName("ROLE_ADMIN")!!)
        userService.addRole(userService.findByUsername("admin")!!, roleService.findByName("ROLE_API")!!)
        userService.addRole(userService.findByUsername("api")!!, roleService.findByName("ROLE_API")!!)

        println("Users and roles created!")
        println(userService.findByUsername("admin")!!)
        println(userService.findByUsername("api")!!)
        println(userService.findByUsername("user")!!)
    }
}