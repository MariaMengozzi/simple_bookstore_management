package bookstore.management.entity

import jakarta.persistence.*
import java.time.Year

@Entity
@Table(name = "book")
data class Book(
    @Id
    val isbn: String,
    val title: String,
    val price: Double,
    val publicationYear: Year, //publication_year
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable( // JoinTable: viene utilizzato sulla proprietà di collezione del lato proprietario della relazione per specificare la tabella di join e le colonne di join.
        name = "book_author",
        joinColumns = [JoinColumn(name = "book_isbn")],
        inverseJoinColumns = [JoinColumn(name = "author_id")]
    )
    val authors: Set<Author> = HashSet(),
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "book_genre",
        joinColumns = [JoinColumn(name = "book_isbn")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    val genres: Set<Genre> = HashSet()
){
    override fun hashCode(): Int {
        return isbn.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Book) return false
        return isbn == other.isbn
    }
}