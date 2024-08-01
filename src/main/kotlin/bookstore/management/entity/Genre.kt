package bookstore.management.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "genre")
data class Genre (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    @ManyToMany (mappedBy = "genres", cascade = [CascadeType.ALL]) //mappedBy: viene utilizzato sulla proprietà di collezione del lato inverso della relazione per indicare che la mappatura è gestita dal lato proprietario.
    @JsonIgnore //evito il loro caricamento in quanto la relazione vede questa entità come inversa - in questo modo non genera un loop infinito
    val books: Set<Book> = HashSet()
){
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Genre) return false
        return id == other.id
    }
}