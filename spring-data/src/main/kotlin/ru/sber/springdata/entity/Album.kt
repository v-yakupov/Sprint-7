package ru.sber.springdata.entity

import javax.persistence.*

@Entity
class Album(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.REFRESH])
    @JoinColumn
    var band: Band? = null,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "album", fetch = FetchType.EAGER, orphanRemoval = true)
    var tracks: MutableList<Track> = mutableListOf()
) {
    fun addTrack(track: Track) {
        track.album = this
        tracks.add(track)
    }

    override fun toString(): String {
        return "Альбом: \"$name\" с треками: $tracks в исполнении \"${band?.name}\""
    }
}