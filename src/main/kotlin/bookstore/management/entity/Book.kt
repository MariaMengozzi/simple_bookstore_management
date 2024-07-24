package bookstore.management.entity

import jakarta.persistence.*
import java.time.Year

@Entity
@Table(name = "book")
data class Book (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val isbn: String,
    val title: String,
    val price: Float,
    val publicationYear: Year, //publication_year
    @ManyToMany
    @JoinTable( // JoinTable: viene utilizzato sulla proprietà di collezione del lato proprietario della relazione per specificare la tabella di join e le colonne di join.
        name = "book_author",
        joinColumns = [JoinColumn(name = "book_isbn")],
        inverseJoinColumns = [JoinColumn(name = "author_id")]
    )
    val authors: Set<Author> = HashSet(),
    @ManyToMany
    @JoinTable(
        name = "book_genre",
        joinColumns = [JoinColumn(name = "book_isbn")],
        inverseJoinColumns = [JoinColumn(name = "genre_id")]
    )
    val genres: Set<Genre> = HashSet()
)