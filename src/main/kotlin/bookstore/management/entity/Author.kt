package bookstore.management.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
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
    @ManyToMany (mappedBy = "authors", cascade = [CascadeType.ALL])
    //mappedBy: viene utilizzato sulla proprietà di collezione del lato inverso della relazione per indicare che la mappatura è gestita dal lato proprietario.
    //con  cascade = [CascadeType.ALL] propago tutte le operazioni anche alle tabelle collegate, mentre con  cascade = [CascadeType.MERGE, CascadeType.PERSIST] solo le operazioni di creazione e update
    @JsonIgnore
    val books: Set<Book> = HashSet()
) {
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Author) return false
        return id == other.id
    }
}
