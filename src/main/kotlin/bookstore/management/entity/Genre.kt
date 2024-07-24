package bookstore.management.entity

import jakarta.persistence.*

@Entity
@Table(name = "genre")
data class Genre (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    @ManyToMany (mappedBy = "genres") //mappedBy: viene utilizzato sulla proprietà di collezione del lato inverso della relazione per indicare che la mappatura è gestita dal lato proprietario.
    val books: Set<Book> = HashSet()
)