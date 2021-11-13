package ru.sber.sbermvc.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
class Role(
    @Id
    @GeneratedValue
    var id: Long = 0,

    var name: String,

    @ManyToMany(mappedBy = "roles")
    var users: MutableSet<User> = mutableSetOf()
) {
    override fun toString(): String {
        return "Role: $name"
    }
}