package ru.sber.springdata.repository

import org.springframework.data.repository.CrudRepository
import ru.sber.springdata.entity.Band

interface BandRepository : CrudRepository<Band, Long> {
}