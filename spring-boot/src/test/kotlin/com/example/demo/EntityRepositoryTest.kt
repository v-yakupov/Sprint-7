package com.example.demo

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.assertEquals

@DataJpaTest
class EntityRepositoryTest {
	@Autowired
	lateinit var entityRepository: EntityRepository

	@Test
	fun `find entity by id`() {
		val set = entityRepository.save(Entity(name = "Pikachu"))
		val get = entityRepository.getById(set.id)

		assertEquals(get, set)
	}


}
