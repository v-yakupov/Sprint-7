package ru.sber.sbermvc.entity

import javax.persistence.*

@Entity
class User(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(unique = true, nullable = false)
    var username: String,

    @Column(nullable = false)
    var password: String,

    @ManyToMany(fetch = FetchType.EAGER)
    var roles: MutableSet<Role> = mutableSetOf()
) {
    override fun toString(): String {
        return "User: $username, pass: $password, roles: $roles"
    }
}