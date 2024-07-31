package bookstore.management.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import java.time.LocalDate
@Entity
@Table(name = "author")
data class Author (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val surname: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val birthday: LocalDate,
    val city: String,
    @ManyToMany (mappedBy = "authors") //mappedBy: viene utilizzato sulla proprietà di collezione del lato inverso della relazione per indicare che la mappatura è gestita dal lato proprietario.
    val books: Set<Book> = HashSet()
)
