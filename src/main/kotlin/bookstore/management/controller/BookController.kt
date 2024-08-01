package bookstore.management.controller

import bookstore.management.entity.Author
import bookstore.management.entity.Book
import bookstore.management.entity.Genre
import bookstore.management.service.BookService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/book")
class BookController (private val service: BookService) : ExceptionController(){
    @GetMapping
    fun getBookByTitle(@RequestParam("title", required = false) title: String?): Collection<Book> =
        if (title != null ) service.getBooksByTitle(title)  else service.getAllBooks()

    @GetMapping("/{isbn}")
    fun getBook(@PathVariable isbn: String): Book = service.getBookByIsbn(isbn)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBook(@RequestBody book: Book) : Book = service.addBook(book)

    @PatchMapping
    fun updateBook(@RequestBody book: Book): Book = service.updateBook(book)

    @DeleteMapping("/{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable isbn: String): Unit = service.deleteBook(isbn)

    @GetMapping("/author")
    fun getAuthorsByBookGenre(@RequestBody genre: Genre) : Collection<Author> = service.getAuthorsByBookGenre(genre)

    @GetMapping("/author/{isbn}")
    fun getBookAuthors(@PathVariable isbn: String) : Collection<Author> = service.getBookAuthors(isbn)

    @GetMapping("/genre/{isbn}")
    fun getBookGenres(@PathVariable isbn: String) : Collection<Genre> = service.getBookGenres(isbn)
}