package bookstore.management.repository

import bookstore.management.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, String>{
    fun findBookByIsbn(isbn: String): Book?
    fun findBookByTitle(title: String): List<Book>
}