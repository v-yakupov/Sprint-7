package ru.sber.springdata

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import ru.sber.springdata.entity.Album
import ru.sber.springdata.entity.Band
import ru.sber.springdata.entity.Track
import ru.sber.springdata.repository.AlbumRepository
import ru.sber.springdata.repository.BandRepository
import kotlin.test.assertEquals

@SpringBootTest
@Sql(statements = [
    "SET REFERENTIAL_INTEGRITY FALSE;",
    "TRUNCATE TABLE Album;",
    "TRUNCATE TABLE Band;",
    "TRUNCATE TABLE Track;",
    "SET REFERENTIAL_INTEGRITY TRUE"],
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SpringDataApplicationTests {

    @Autowired
    lateinit var albumRepository: AlbumRepository
    @Autowired
    lateinit var bandRepository: BandRepository

    @Test
    fun `should create band, albums and records`() {
        val bandGiven1 = Band(name = "Кот Баюн")
        val albumGiven1 = Album(name = "Было время...")
        val albumGiven2 = Album(name = "Сильнее зла")
        albumGiven1.addTrack(Track(title = "Ветер"))
        albumGiven2.addTrack(Track(title = "Солнцестояние"))
        bandGiven1.addAlbum(albumGiven1)
        bandGiven1.addAlbum(albumGiven2)
        bandRepository.save(bandGiven1)

        assertEquals(1, bandRepository.findAll().toList().size)
        assertEquals(2, bandRepository.findAll().toList()[0].albums.size)
        assertEquals(1, bandRepository.findAll().toList()[0].albums[0].tracks.size)
        assertEquals(1, bandRepository.findAll().toList()[0].albums[1].tracks.size)
    }

    @Test
    fun `should add records`() {
        val bandGiven1 = Band(name = "Кот Баюн")
        val albumGiven1 = Album(name = "Было время...")
        val albumGiven2 = Album(name = "Сильнее зла")
        albumGiven1.addTrack(Track(title = "Ветер"))
        albumGiven2.addTrack(Track(title = "Солнцестояние"))
        bandGiven1.addAlbum(albumGiven1)
        bandGiven1.addAlbum(albumGiven2)
        bandRepository.save(bandGiven1)

        albumGiven2.addTrack(Track(title = "Кикимора"))
        albumGiven2.addTrack(Track(title = "Молитва перед боем"))
        albumRepository.save(albumGiven2)

        assertEquals(1, bandRepository.findAll().toList().size)
        assertEquals(2, bandRepository.findAll().toList()[0].albums.size)
        assertEquals(1, bandRepository.findAll().toList()[0].albums[0].tracks.size)
        assertEquals(3, bandRepository.findAll().toList()[0].albums[1].tracks.size)
    }

    @Test
    fun `should remove band and albums`() {
        val bandGiven1 = Band(name = "Кот Баюн")
        val albumGiven1 = Album(name = "Было время...")
        val albumGiven2 = Album(name = "Сильнее зла")

        albumGiven1.addTrack(Track(title = "Ветер"))
        albumGiven2.addTrack(Track(title = "Солнцестояние"))
        albumGiven2.addTrack(Track(title = "Кикимора"))
        albumGiven2.addTrack(Track(title = "Молитва перед боем"))
        bandGiven1.addAlbum(albumGiven1)
        bandGiven1.addAlbum(albumGiven2)
        bandRepository.save(bandGiven1)
        bandRepository.delete(bandGiven1)

        assertEquals(0, bandRepository.findAll().toList().size)
        assertEquals(0, albumRepository.findAll().toList().size)
    }

}
