package bookstore.management.repository

import bookstore.management.entity.Genre
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class GenreRepositoryTest @Autowired constructor(
    val genreRepository: GenreRepository,
){

    @Test
    fun `should save and retrieve some genres`() {
        val genres: Collection<Genre> = setOf(
            Genre(name = "Thriller"),
            Genre(name = "Adventure"),
            Genre(name = "Mystery")
        )
        genreRepository.saveAll(genres)

        val foundGenres = genreRepository.findAll()

        assertTrue(foundGenres.containsAll(genres))

    }
}