package ru.sber.orm.entity

import javax.persistence.*

@Entity
class Band(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "band", fetch = FetchType.EAGER, orphanRemoval = true)
    var albums: MutableList<Album> = mutableListOf()
) {
    fun addAlbum(album: Album) {
        album.band = this
        albums.add(album)
    }

    override fun toString(): String {
        return "Группа: \"$name\" с альбомами: $albums"
    }
}