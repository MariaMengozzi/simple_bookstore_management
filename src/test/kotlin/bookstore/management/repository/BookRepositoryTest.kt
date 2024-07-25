package bookstore.management.repository


import bookstore.management.entity.Book
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.time.Year


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class BookRepositoryTest @Autowired constructor(
    val bookRepository: BookRepository,
){
    @Test
    fun `should save and retrieve a book by isbn`() {

        val book = bookRepository.save(
            Book(
                isbn = "1405862335",
                title = "A scandal in Bohemia",
                price = 13.32,
                publicationYear = Year.of(1891)
            )
        )

        val foundBook = bookRepository.findBookByIsbn("1405862335")

        assertEquals(book, foundBook)
    }

    @Test
    fun `should retrieve books by title`() {


        val book1 = bookRepository.save(
            Book(
                isbn = "1",
                title = "book",
                price = 13.32,
                publicationYear = Year.of(1891),
            )
        )

        val book2 = bookRepository.save(
            Book(
                isbn = "2",
                title = "book",
                price = 13.32,
                publicationYear = Year.of(1891),
            )
        )

        val foundBook = bookRepository.findBooksByTitle("book")

        assertTrue(foundBook.containsAll(listOf(book1, book2)))
    }

    @Test
    fun `should not retrieve any book`() {

        val foundBook = bookRepository.findBookByIsbn("1405862335")
        assertNull(foundBook)

    }
}