package bookstore.management.controller

import bookstore.management.entity.Genre
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

@SpringBootTest
@Sql(scripts = ["classpath:sql/bookstore.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureMockMvc
class GenreControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    val baseUrl = "/genre"

    @Nested
    @DisplayName("GET: genre/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)

    inner class GetAuthors {
        @Test
        fun `should return all genres`() {

            mockMvc.get(baseUrl)
                .andDo{print()}
                .andExpect{
                    status {isOk()}
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[?(@.name =~ /Thriller|Adventure|Fantasy/)]"){exists()}
                }
        }
    }

    @Nested
    @DisplayName("GET: genre/{genreId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetGenre {
        @Test
        fun `should return the genre with given id`() {
            val genre = Genre(
                id = 1,
                name = "Thriller"
            )
            mockMvc.get(baseUrl + "/" + genre.id)
                .andDo{print()}
                .andExpect{
                    status {isOk()}
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(genre))
                    }
                }
        }
    }

    @Nested
    @DisplayName("GET - NOT FOUND: genre/{genreId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetGenreException {
        @Test
        fun `should rise not found exception due to not existing genre id`() {
            val genre = Genre(
                id = -1,
                name = "not existing"
            )
            mockMvc.get(baseUrl + "/" + genre.id)
                .andDo{print()}
                .andExpect{
                    status {isNotFound()}
                }
        }
    }

    @Nested
    @DisplayName("POST: genre/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostAuthor {
        @Test
        fun `should add a new genre`() {

            // given
            val newGenre = Genre(
                id = 4,
                name = "Mystery"
            )

            val performPost =
                mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newGenre)
            }

            performPost.andDo{print()}
            .andExpect{
                status {isCreated()}
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(newGenre))
                }
            }
        }
    }

    @Nested
    @DisplayName("POST - ARGUMENT EXCEPTION: genre/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostExistingAuthor {
        @Test
        fun `should rise a illegal argument exception`() {
            // given
            val stillExistingGenre = Genre(
                id = 1,
                name = "Mystery"
            )

            val performPost =
                mockMvc.post(baseUrl){
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(stillExistingGenre)
                }

            performPost
                .andDo{print()}
                .andExpect{
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    @DisplayName("PATCH: genre/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchAuthor {
        @Test
        fun `should update a genre`() {
            // given
            val updateGenre = Genre(
                id = 1,
                name = "Mystery"
            )

            val performPost =
                mockMvc.patch(baseUrl){
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updateGenre)
                }

            performPost
                .andDo{print()}
                .andExpect{
                    status {
                        status { isOk() }
                        content {
                            contentType(MediaType.APPLICATION_JSON)
                            json(objectMapper.writeValueAsString(updateGenre))
                        }
                    }
                }

            mockMvc.get("$baseUrl/${updateGenre.id}")
                .andExpect {
                    content {
                        json(objectMapper.writeValueAsString(updateGenre))
                    }
                }
        }
    }

    @Nested
    @DisplayName("PATCH - NOT FOUND: genre/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchNoFoundAuthor {
        @Test
        fun `should rise a not found exception due to updating a non existing genre`() {
            // given
            val updateGenre = Genre(
                id = -1,
                name = "Mystery"
            )

            val performPost =
                mockMvc.patch(baseUrl){
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updateGenre)
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
    @DisplayName("DELETE: /genre/{genreId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteAuthor {
        @Test
        fun `should delete the genre with the given id`() {
            val id = 2
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
    @DisplayName("DELETE - NOT FOUND: /genre/{genreId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteNotFoundAuthor {
        @Test
        fun `should rise a not found exception due to deleting a non existing genre`() {
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