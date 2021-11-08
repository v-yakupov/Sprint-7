package ru.sber.springdata.repository

import org.springframework.data.repository.CrudRepository
import ru.sber.springdata.entity.Album

interface AlbumRepository : CrudRepository<Album, Long> {
}