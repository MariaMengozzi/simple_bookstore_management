package bookstore.management.service

import bookstore.management.entity.Author
import bookstore.management.repository.AuthorRepository
import io.mockk.every
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull


class AuthorServiceTest {

    private val authorRepository = mockk<AuthorRepository>(relaxUnitFun = true)
    private val authorService = AuthorService(authorRepository)

    @Test
    fun `should call its data source to retrieve authors`() {
        //given
        every { authorRepository.findAll() } returns emptyList()

        // when
        authorService.getAllAuthors()
        // then
        verify(exactly = 1) {authorRepository.findAll()}
    }

    @Test
    fun `should create an author`() {
        // given

        val author =  Author(
            name = "Arthur",
            surname = "Conan Doyle",
            birthdate = LocalDate.of(1859, 5, 22),
            city = "Edinburgh",
        )

        every { authorRepository.findByIdOrNull(author.id) } returns emptyList<Author>().find { it.id == author.id }
        every { authorRepository.save(author) } returns author

        // when
        val newAuthor = authorService.addAuthor(author)

        // then
        assertEquals(author, newAuthor)
    }

    @Test
    fun `should rise an illegal argument exception due to a still existing author`() {
        // given

        val author =  Author(
            name = "Arthur",
            surname = "Conan Doyle",
            birthdate = LocalDate.of(1859, 5, 22),
            city = "Edinburgh",
        )

        every { authorRepository.findByIdOrNull(author.id) } returns listOf(author).find { it.id == author.id }
        every { authorRepository.save(author) } returns author

        assertThrows<IllegalArgumentException> { authorService.addAuthor(author) }
    }

    @Test
    fun `should get All Authors`() {
        val authors = listOf(
            Author(
                name = "Arthur",
                surname = "Conan Doyle",
                birthdate = LocalDate.of(1859, 5, 22),
                city = "Edinburgh",
            ),
            Author(
                name = "Agatha",
                surname = "Christie",
                birthdate = LocalDate.of(1890, 9, 15),
                city = "Torquay"
            ),
            Author(
                name = "J.K.",
                surname = "Rowling",
                birthdate = LocalDate.of(1965, 7, 31),
                city = "Yate"
            )
        )

        every { authorRepository.findAll() } returns authors

        val foundAuthors = authorService.getAllAuthors()
        assertEquals(authors.size, foundAuthors.size)
        assertTrue(foundAuthors.containsAll(authors))
        verify(exactly = 1) { authorService.getAllAuthors() }

    }
}