package bookstore.management.repository

import bookstore.management.entity.Genre
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GenreRepository : JpaRepository<Genre, Long>{
    fun findGenreByName(name: String): Genre?
}