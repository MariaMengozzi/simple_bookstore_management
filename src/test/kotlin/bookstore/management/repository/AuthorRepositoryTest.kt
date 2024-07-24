package bookstore.management.repository

import bookstore.management.TestcontainersConfiguration
import bookstore.management.entity.Author
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate

@SpringJUnitConfig(TestcontainersConfiguration::class)
@SpringBootTest
@Sql(scripts = ["classpath:bookstore.sql"])
@Testcontainers
@Transactional //This ensures that the session remains open until the transaction completes2.
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
}