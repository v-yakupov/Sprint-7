package ru.sber.orm.dao

import org.hibernate.SessionFactory
import ru.sber.orm.entity.Band

class BandDao(private val sessionFactory: SessionFactory) {
    fun save(band: Band) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.save(band)
            it.transaction.commit()
        }
    }

    fun update(band: Band) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.update(band)
            it.transaction.commit()
        }
    }

    fun findById(id: Long): Band? {
        val res: Band?
        sessionFactory.openSession().use {
            it.beginTransaction()
            res = it.get(Band::class.java, id)
            it.transaction.commit()
        }
        return res
    }

    fun findAll(): List<Band> {
        val res: List<Band>
        sessionFactory.openSession().use {
            it.beginTransaction()
            res = it.session.createQuery("FROM Band").list() as List<Band>
            it.transaction.commit()
        }
        return res
    }

    fun delete(band: Band) {
        sessionFactory.openSession().use {
            it.beginTransaction()
            it.session.delete(band)
            it.transaction.commit()
        }
    }
}