package bookstore.management.service

import bookstore.management.entity.Author
import bookstore.management.repository.AuthorRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthorService (private val repository: AuthorRepository){

    fun getAllAuthors(): Collection<Author> = repository.findAll()

    fun getAuthorById(id: Long): Author = repository.findByIdOrNull(id)
        ?: throw NoSuchElementException("Could not find an author with id $id")

    fun addAuthor(author: Author): Author = repository.findByIdOrNull(author.id)
        ?.let {
            throw IllegalArgumentException ("Author with id $author.id already exist")
        } ?: repository.save(author)

    fun updateAuthor(author: Author): Author = repository.findByIdOrNull(author.id)?.let {
        repository.save(author)
    } ?: throw NoSuchElementException("Could not find an author with id $author.id")

    fun deleteAuthor(id: Long): Unit = repository.findByIdOrNull(id)?.let { repository.deleteById(id) }
        ?: throw NoSuchElementException("Could not find an author with id $id")
}