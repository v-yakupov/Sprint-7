package ru.sber.orm.dao

import org.hibernate.SessionFactory
import ru.sber.orm.entity.Album

class AlbumDao(private val sessionFactory: SessionFactory) {
    fun save(album: Album) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.save(album)
            it.transaction.commit()
        }
    }

    fun update(album: Album) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.update(album)
            it.transaction.commit()
        }
    }

    fun findById(id: Long): Album? {
        val res: Album?
        sessionFactory.openSession().use {
            it.beginTransaction()
            res = it.get(Album::class.java, id)
            it.transaction.commit()
        }
        return res
    }

    fun findAll(): List<Album> {
        val res: List<Album>
        sessionFactory.openSession().use {
            it.beginTransaction()
            res = it.session.createQuery("FROM Album").list() as List<Album>
            it.transaction.commit()
        }
        return res
    }

    fun delete(album: Album) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.session.delete(album)
            it.transaction.commit()
        }
    }
}