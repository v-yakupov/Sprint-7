package ru.sber.orm

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import ru.sber.orm.dao.AlbumDao
import ru.sber.orm.dao.BandDao
import ru.sber.orm.entity.Album
import ru.sber.orm.entity.Band
import ru.sber.orm.entity.Track
import kotlin.test.assertEquals

@SpringBootTest
@Sql(statements = [
    "SET REFERENTIAL_INTEGRITY FALSE;",
    "TRUNCATE TABLE Album;",
    "TRUNCATE TABLE Band;",
    "TRUNCATE TABLE Track;",
    "SET REFERENTIAL_INTEGRITY TRUE"],
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrmApplicationTests {
    private val sessionFactory: SessionFactory = Configuration().configure()
        .addAnnotatedClass(Album::class.java)
        .addAnnotatedClass(Track::class.java)
        .addAnnotatedClass(Band::class.java)
        .buildSessionFactory()

    val albumDao = AlbumDao(sessionFactory)
    val bandDao = BandDao(sessionFactory)

    @Test
    fun `should create band, albums and records`() {
        val bandGiven1 = Band(name = "Кот Баюн")
        val albumGiven1 = Album(name = "Было время...")
        val albumGiven2 = Album(name = "Сильнее зла")
        albumGiven1.addTrack(Track(title = "Ветер"))
        albumGiven2.addTrack(Track(title = "Солнцестояние"))
        bandGiven1.addAlbum(albumGiven1)
        bandGiven1.addAlbum(albumGiven2)
        bandDao.save(bandGiven1)

        assertEquals(1, bandDao.findAll().toList().size)
        assertEquals(2, bandDao.findAll().toList()[0].albums.size)
        assertEquals(1, bandDao.findAll().toList()[0].albums[0].tracks.size)
        assertEquals(1, bandDao.findAll().toList()[0].albums[1].tracks.size)
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
        bandDao.save(bandGiven1)

        albumGiven2.addTrack(Track(title = "Кикимора"))
        albumGiven2.addTrack(Track(title = "Молитва перед боем"))
        albumDao.update(albumGiven2)

        assertEquals(1, bandDao.findAll().toList().size)
        assertEquals(2, bandDao.findAll().toList()[0].albums.size)
        assertEquals(1, bandDao.findAll().toList()[0].albums[0].tracks.size)
        assertEquals(3, bandDao.findAll().toList()[0].albums[1].tracks.size)
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
        bandDao.save(bandGiven1)
        bandDao.delete(bandGiven1)

        assertEquals(0, bandDao.findAll().toList().size)
        assertEquals(0, albumDao.findAll().toList().size)
    }
}
