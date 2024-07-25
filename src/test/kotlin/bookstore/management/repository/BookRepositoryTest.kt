package bookstore.management.repository

import bookstore.management.entity.Author
import bookstore.management.entity.Book
import bookstore.management.entity.Genre
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.time.Year
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class BookRepositoryTest @Autowired constructor(
    val bookRepository: BookRepository,
    val authorRepository: AuthorRepository,
    val genreRepository: GenreRepository
){
    @Test
    fun `should save and retrieve a book by isbn`() {
        val author = authorRepository.save(
            Author(
                name = "Arthur",
                surname = "Conan Doyle",
                birthdate = LocalDate.of(1859, 5, 22),
                city = "Edinburgh",
            )
        )

        val genres = setOf(
                Genre(name = "Noir"),
                Genre(name = "Thriller"),
                Genre(name = "Mystery")
            )

        genreRepository.saveAll(genres)


        val book = bookRepository.save(
            Book(
                isbn = "1405862335",
                title = "A scandal in Bohemia",
                price = 13.32,
                publicationYear = Year.of(1891),
                authors = setOf(author),
                genres = genres
            )
        )

        val foundBook = bookRepository.findBookByIsbn("1405862335")

        assertEquals(book, foundBook)
    }

    @Test
    fun `should retrieve books by title`() {
        val author1 = authorRepository.save(
            Author(
                name = "a",
                surname = "1",
                birthdate = LocalDate.of(1859, 5, 22),
                city = "Edinburgh",
            )
        )

        val author2 = authorRepository.save(
            Author(
                name = "a",
                surname = "2",
                birthdate = LocalDate.of(1859, 5, 22),
                city = "Edinburgh",
            )
        )

        val genres = setOf(
            Genre(name = "Noir"),
            Genre(name = "Thriller"),
            Genre(name = "Mystery")
        )

        genreRepository.saveAll(genres)


        val book1 = bookRepository.save(
            Book(
                isbn = "1",
                title = "book",
                price = 13.32,
                publicationYear = Year.of(1891),
                authors = setOf(author1),
                genres = genres
            )
        )

        val book2 = bookRepository.save(
            Book(
                isbn = "2",
                title = "book",
                price = 13.32,
                publicationYear = Year.of(1891),
                authors = setOf(author2),
                genres = genres
            )
        )

        val foundBook = bookRepository.findBookByTitle("book")

        assertTrue(foundBook.containsAll(listOf(book1, book2)))
    }

    @Test
    fun `should not retrieve any book`() {

        val foundBook = bookRepository.findBookByIsbn("1405862335")
        assertNull(foundBook)

    }
}