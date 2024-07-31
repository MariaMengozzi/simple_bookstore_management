package bookstore.management.controller

import bookstore.management.entity.Genre
import bookstore.management.service.GenreService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/genre")
class GenreController (private val service: GenreService) : ExceptionController(){
    @GetMapping
    fun getGenres(): Collection<Genre> = service.getAllGenres()

    @GetMapping("/{genreId}")
    fun getGenre(@PathVariable genreId: Long): Genre = service.getGenreById(genreId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addGenre(@RequestBody genre: Genre) : Genre = service.addGenre(genre)

    @PatchMapping
    fun updateGenre(@RequestBody genre: Genre): Genre = service.updateGenre(genre)

    @DeleteMapping("/{genreId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteGenre(@PathVariable genreId: Long): Unit = service.deleteGenre(genreId)
}