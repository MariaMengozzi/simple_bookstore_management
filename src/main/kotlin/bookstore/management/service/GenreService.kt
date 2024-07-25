package bookstore.management.service

import bookstore.management.entity.Genre
import bookstore.management.repository.GenreRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class GenreService (private val repository: GenreRepository){

    fun getAllGenres(): Collection<Genre> = repository.findAll()

    fun getGenreById(id: Long): Genre = repository.findByIdOrNull(id)
        ?: throw NoSuchElementException("Could not find an genre with id $id")

    fun addGenre(genre: Genre) : Genre = repository.findByIdOrNull(genre.id)
        ?.let {
            throw IllegalArgumentException ("Genre with id $genre.id already exist")
        } ?: repository.save(genre)

    fun updateGenre(genre: Genre): Genre = repository.findByIdOrNull(genre.id)?.let {
        repository.deleteById(genre.id)
        repository.save(genre)
    } ?: throw NoSuchElementException("Could not find a genre with id $genre.id")

    fun deleteGenre(id: Long): Unit = repository.findByIdOrNull(id)?.let { repository.deleteById(id) }
        ?: throw NoSuchElementException("Could not find a genre with id $id")
}