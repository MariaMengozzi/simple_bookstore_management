package bookstore.management.repository

import bookstore.management.entity.Author
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

import java.time.LocalDate
import kotlin.jvm.optionals.getOrNull

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AuthorRepositoryTest @Autowired constructor(
    val authorRepository: AuthorRepository,
){

    @Test
    fun `should save and retrieve an author`() {
        val author = authorRepository.save(
            Author(
                name = "Arthur",
                surname = "Conan Doyle",
                birthdate = LocalDate.of(1859, 5, 22),
                city = "Edinburgh",
            )
        )

        val foundAuthor: Author = authorRepository.findById(author.id).get()
        assertEquals(author, foundAuthor)
    }

    @Test
    fun `should not found any author with not existing id`() {

        val foundAuthor: Author? = authorRepository.findById(-1234).getOrNull()
        assertNull(foundAuthor)
    }
}