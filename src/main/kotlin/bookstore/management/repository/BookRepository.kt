package bookstore.management.repository

import bookstore.management.entity.Author
import bookstore.management.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, String>{
    fun findBookByIsbn(isbn: String): Book?
    fun findBooksByTitle(title: String): List<Book>
    @Query(
        "SELECT DISTINCT a " +
                "FROM Author a " +
                "JOIN a.books b " +
                "JOIN b.genres g " +
                "WHERE g.id = :genre"
    )
    fun findAuthorsByBookGenre(@Param("genre") genre: Long): List<Author>}