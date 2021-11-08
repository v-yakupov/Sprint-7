package ru.sber.orm.entity

import javax.persistence.*

@Entity
class Track(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(nullable = false)
    var title: String,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.REFRESH])
    @JoinColumn
    var album: Album? = null
) {
    override fun toString(): String {
        return "Трек: \"$title\" в \"${album?.name ?: "Сингл"}\""
    }
}