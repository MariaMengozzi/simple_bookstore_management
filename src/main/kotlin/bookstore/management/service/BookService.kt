package bookstore.management.service

import bookstore.management.entity.Author
import bookstore.management.entity.Book
import bookstore.management.entity.Genre
import bookstore.management.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService (private val repository: BookRepository){

    fun getAllBooks(): Collection<Book> = repository.findAll()

    fun getBookByIsbn(isbn: String): Book = repository.findBookByIsbn(isbn)
        ?: throw NoSuchElementException("Could not find a book with isbn $isbn")

    fun getBooksByTitle(title: String): Collection<Book> = repository.findBooksByTitle(title)

    fun getAuthorsByBookGenre(genre: Genre): Collection<Author> = repository.findAuthorsByBookGenre(genre.id)

    fun addBook(book: Book) : Book = repository.findBookByIsbn(book.isbn)
        ?.let {
            throw IllegalArgumentException ("Author with id $book.isbn already exist")
        } ?: repository.save(book)

    fun updateBook(book: Book): Book = repository.findBookByIsbn(book.isbn)?.let {
        repository.save(book)
    } ?: throw NoSuchElementException("Could not find a book with account number $book.isbn")

    fun deleteBook(isbn: String): Unit = repository.findBookByIsbn(isbn)?.let { repository.deleteById(isbn) }
        ?: throw NoSuchElementException("Could not find a book with isbn $isbn")

    fun getBookAuthors(isbn: String): Collection<Author> = repository.findBookByIsbn(isbn)?.let { repository.findBookAuthors(isbn) }
        ?: throw NoSuchElementException("Could not find a book with isbn $isbn")

    fun getBookGenres(isbn: String): Collection<Genre> = repository.findBookByIsbn(isbn)?.let { repository.findBookGenres(isbn) }
        ?: throw NoSuchElementException("Could not find a book with isbn $isbn")
}