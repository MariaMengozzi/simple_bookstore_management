package bookstore.management.controller

import bookstore.management.entity.Author
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.*
import java.time.LocalDate

@SpringBootTest
@Sql(scripts = ["classpath:sql/bookstore.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureMockMvc
class AuthorControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    val baseUrl = "/author"

    @Nested
    @DisplayName("GET: author/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)

    inner class GetAuthors {
        @Test
        fun `should return all authors`() {

            mockMvc.get(baseUrl)
                .andDo{print()}
                .andExpect{
                    status {isOk()}
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[?(@.name =~ /Arthur|Agata|J.K|George/)]"){exists()}
                }
        }
    }

    @Nested
    @DisplayName("GET: author/{authorId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAuthor {
        @Test
        fun `should return the author with id`() {
            val author = Author(
                id = 1,
                name = "Arthur",
                surname = "Conan Doyle",
                birthday = LocalDate.of(1859, 5, 22),
                city = "Edinburgh",
            )
            mockMvc.get(baseUrl + "/" + author.id)
                .andDo{print()}
                .andExpect{
                    status {isOk()}
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(author))
                    }
                }
        }
    }

    @Nested
    @DisplayName("GET - NOT FOUND: author/{authorId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAuthorException {
        @Test
        fun `should return all authors`() {
            val author = Author(
                id = -1,
                name = "Author",
                surname = "Not existing",
                birthday = LocalDate.of(1859, 5, 22),
                city = "None",
            )
            mockMvc.get(baseUrl + "/" + author.id)
                .andDo{print()}
                .andExpect{
                    status {isNotFound()}
                }
        }
    }

    @Nested
    @DisplayName("POST: author/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostAuthor {
        @Test
        fun `should add a new author`() {
            /*TODO il problema è il seguente.. con il db pre configurato spring cerca di reinserire gli id a partire da
            default + 1 quindi 1, questo fa si che si si generi un errore relativo al tentativo di impostare io manualmente
            un id, questo non accade se il db è vuoto e quindi inizio ad utilizzare la entity della sessione, che inizierà
            da 1 (default + 1) e per ogni save aggiungerà 1 conviene quindi trovare un modo per non inizializzare in questo test il db
            oppure un modo per inizializzare il repository in modo che parta dall'ultimo id presente all'interno del database

            Problema 2, non mi fa creare delle entità custom che abbiano un id già presente

             */

            // given
            val newAuthor = Author(
                id = 5,
                name = "Federico",
                surname = "Inverni",
                birthday = LocalDate.of(1970, 9, 15),
                city = "Rome",
            )

            val performPost =
                mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newAuthor)
            }

            performPost.andDo{print()}
            .andExpect{
                status {isCreated()}
                content {
                    contentType(MediaType.APPLICATION_JSON) //confronto solo i dati dell'autore e non l'id
                    jsonPath("$.name"){newAuthor.name}
                    jsonPath("$.surname"){newAuthor.surname}
                    jsonPath("$.birthday"){newAuthor.birthday}
                    jsonPath("$.city"){newAuthor.city}
                }
            }
        }
    }

    @Nested
    @DisplayName("POST - ARGUMENT EXCEPTION: author/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostExistingAuthor {
        @Test
        fun `should rise a illegal argument exception`() {
            // given
            val stillExistingAuthor = Author(
                id = 2,
                name = "Federico",
                surname = "Inverni",
                birthday = LocalDate.of(1970, 9, 15),
                city = "Rome",
            )

            val performPost =
                mockMvc.post(baseUrl){
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(stillExistingAuthor)
                }

            performPost
                .andDo{print()}
                .andExpect{
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    @DisplayName("PATCH: author/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchAuthor {
        @Test
        fun `should update an author`() {
            // given
            val updateAuthor = Author(
                id = 2,
                name = "Federico",
                surname = "Inverni",
                birthday = LocalDate.of(1970, 9, 15),
                city = "Rome",
            )

            val performPost =
                mockMvc.patch(baseUrl){
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updateAuthor)
                }

            performPost
                .andDo{print()}
                .andExpect{
                    status {
                        status { isOk() }
                        content {
                            contentType(MediaType.APPLICATION_JSON)
                            json(objectMapper.writeValueAsString(updateAuthor)) //we are checking the input that is in the body instead checking all single object property
                        }
                    }
                }

            //we verify that the bank is correctly updated using the route get > api/banks/{accountNum}
            mockMvc.get("$baseUrl/${updateAuthor.id}")
                .andExpect {
                    content {
                        json(objectMapper.writeValueAsString(updateAuthor))
                    }
                }
        }
    }

    @Nested
    @DisplayName("PATCH - NOT FOUND: author/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchNoFoundAuthor {
        @Test
        fun `should rise a not found exception due to updating a non existing author`() {
            // given
            val updateAuthor = Author(
                id = -1,
                name = "Federico",
                surname = "Inverni",
                birthday = LocalDate.of(1970, 9, 15),
                city = "Rome",
            )

            val performPost =
                mockMvc.patch(baseUrl){
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updateAuthor)
                }

            performPost
                .andDo{print()}
                .andExpect{
                    status {
                        status { isNotFound() }
                    }
                }
        }
    }

    @Nested
    @DisplayName("DELETE: /author/{authorId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteAuthor {
        @Test
        fun `should delete the author with the given id`() {
            val id = 3
            //when/then
            mockMvc.delete("$baseUrl/$id")
                .andDo{print()}
                .andExpect{
                    status {isNoContent()}
                }

            //check that is actually deleted
            mockMvc.get("$baseUrl/${id}")
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("DELETE - NOT FOUND: /author/{authorId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteNotFoundAuthor {
        @Test
        fun `should rise a not found exception due to deleting a non existing author`() {
            val id = -1
            //when/then
            mockMvc.delete("$baseUrl/$id")
                .andDo{print()}
                .andExpect{
                    status {isNotFound()}
                }
        }
    }

}