package bookstore.management.service

import bookstore.management.repository.BookRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class BookServiceTest {
    private val bookRepository = mockk<BookRepository>(relaxUnitFun = true)
    private val bookService = BookService(bookRepository)

    @Test
    fun `should call its data source to retrieve authors`() {
        //given
        every { bookRepository.findAll() } returns emptyList()

        // when
        bookService.getAllBooks()
        // then
        verify(exactly = 1) {bookRepository.findAll()}
    }
}