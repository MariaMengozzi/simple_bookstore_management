package bookstore.management.controller

import bookstore.management.entity.Author
import bookstore.management.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/author")
class AuthorController (private val service: AuthorService) : ExceptionController() {

    @GetMapping
    fun getAuthors(): Collection<Author> = service.getAllAuthors()

    @GetMapping("/{authorId}")
    fun getAuthor(@PathVariable authorId: Long): Author = service.getAuthorById(authorId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addAuthor(@RequestBody author: Author) : Author = service.addAuthor(author)

    @PatchMapping
    fun updateAuthor(@RequestBody author: Author): Author = service.updateAuthor(author)

    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAuthor(@PathVariable authorId: Long): Unit = service.deleteAuthor(authorId)

}