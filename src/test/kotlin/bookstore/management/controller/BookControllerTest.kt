package bookstore.management.controller

import bookstore.management.entity.Book
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.*
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
import java.time.Year

@SpringBootTest
@Sql(scripts = ["classpath:sql/bookstore.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureMockMvc
class BookControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    val baseUrl = "/book"

    @Nested
    @DisplayName("GET: book/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBooks {
        @Test
        fun `should return all books`() {
            mockMvc.get(baseUrl)
                .andDo{print()}
                .andExpect{
                    status {isOk()}
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[?(@.isbn =~ /8804780169|8883377311|880472868X|8883379071/)]"){exists()}
                }
        }
    }

    @Nested
    @DisplayName("GET: book/{isbn}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBook {
        @Test
        fun `should return the book with given isbn`() {
            val isbn =  "8883377311"
            val title = "Le avventure di Sherlock Holmes"
            mockMvc.get("$baseUrl/$isbn")
                .andDo{print()}
                .andExpect{
                    status {isOk()}
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.isbn"){isbn}
                        jsonPath("$.title"){title}
                    }
                }
        }
    }

    @Nested
    @DisplayName("GET - NOT FOUND: book/{genreId}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBookException {
        @Test
        fun `should rise not found exception due to not existing book isbn`() {
            val isbn = "not existing isbn"
            mockMvc.get("$baseUrl/$isbn")
                .andDo{print()}
                .andExpect{
                    status {isNotFound()}
                }
        }
    }

    @Nested
    @DisplayName("GET: book?title={title}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBookByTitle {
        @Test
        fun `should return a collection of books with given title`() {
            val isbn =  "8883377311"
            val title = "Le avventure di Sherlock Holmes"
            mockMvc.get("$baseUrl?title={title}", title)
                .andDo{print()}
                .andExpect{
                    status {isOk()}
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$[0].isbn"){isbn}
                    }
                }
        }
    }

    @Nested
    @DisplayName("POST: book/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostBook {
        @Test
        fun `should add a new genre`() {

            // given
            val newBook = Book(
                isbn = "8883379072",
                title = "new book",
                price = 9.0,
                publicationYear = Year.of(1891)
            )

            val performPost =
                mockMvc.post(baseUrl){
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBook)
            }

            performPost.andDo{print()}
            .andExpect{
                status {isCreated()}
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(newBook))
                }
            }
        }
    }

    @Nested
    @DisplayName("POST - ARGUMENT EXCEPTION: book/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PostExistingAuthor {
        @Test
        fun `should rise a illegal argument exception`() {
            // given
            val book = Book(
                isbn = "8883379071",
                title = "new title",
                price = 9.0,
                publicationYear = Year.of(1891)
            )

            val performPost =
                mockMvc.post(baseUrl){
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(book)
                }

            performPost
                .andDo{print()}
                .andExpect{
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    @DisplayName("PATCH: book/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchBook {
        @Test
        fun `should update a book`() {
            // given
            val updateBook = Book(
                isbn = "8883379071",
                title = "new title",
                price = 9.0,
                publicationYear = Year.of(1891)
            )

            val performPost =
                mockMvc.patch(baseUrl){
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updateBook)
                }

            performPost
                .andDo{print()}
                .andExpect{
                    status {
                        status { isOk() }
                        content {
                            contentType(MediaType.APPLICATION_JSON)
                            json(objectMapper.writeValueAsString(updateBook))
                        }
                    }
                }

            mockMvc.get("$baseUrl/${updateBook.isbn}")
                .andExpect {
                    content {
                        json(objectMapper.writeValueAsString(updateBook))
                    }
                }
        }
    }

    @Nested
    @DisplayName("PATCH - NOT FOUND: book/")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class PatchNoFoundBook {
        @Test
        fun `should rise a not found exception due to updating a non existing book`() {
            // given
            val updateBook = Book(
                isbn = "not existing",
                title = "new title",
                price = 9.0,
                publicationYear = Year.of(1891)
            )

            val performPost =
                mockMvc.patch(baseUrl){
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(updateBook)
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
    @DisplayName("DELETE: /book/{isbn}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteBook {
        @Test
        fun `should delete the book with the given isbn`() {
            val isbn = "8883379071"
            //when/then
            mockMvc.delete("$baseUrl/$isbn")
                .andDo{print()}
                .andExpect{
                    status {isNoContent()}
                }

            //check that is actually deleted
            mockMvc.get("$baseUrl/${isbn}")
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("DELETE - NOT FOUND: /book/{isbn}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class DeleteNotFoundBook {
        @Test
        fun `should rise a not found exception due to deleting a non existing book`() {
            val isbn = "not existing isbn"
            //when/then
            mockMvc.delete("$baseUrl/$isbn")
                .andDo{print()}
                .andExpect{
                    status {isNotFound()}
                }
        }
    }

    @Nested
    @DisplayName("GET: book/author")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetAuthorsByBookGenre {
        @Test
        fun `should return the all author that wrote a book with the given genre`() {
            mockMvc.get("$baseUrl/author")
            {
                contentType = MediaType.APPLICATION_JSON
                content = """
                    {
                        "id" : 0,
                        "name" : "Thriller"
                    }
                    """
            }
                .andDo{print()}
                .andExpect{
                    status {isOk()}
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.*"){hasSize<Any>(2)}
                        jsonPath("$[*].name"){containsInAnyOrder("Arthur", "Agatha")}
                        jsonPath("$[*].name"){ not(contains("George")) }
                    }
                }
        }
    }

    /*
    @GetMapping("/genre/{isbn}")
    fun getBookGenres(@PathVariable isbn: String) : Collection<Genre> = service.getBookGenres(isbn)*/

    @Nested
    @DisplayName("GET: book/author/{isbn}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBookAuthors {
        @Test
        fun `should return all the authors of a book`() {
            val isbn = "8883377311"
            mockMvc.get("$baseUrl/author/$isbn")
                .andDo{print()}
                .andExpect{
                    status {isOk()}
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.*"){hasSize<Any>(1)}
                        jsonPath("$[*].name"){containsInAnyOrder("Arthur")}
                        jsonPath("$[*].name"){ not(containsInAnyOrder("George", "Agatha")) }
                    }
                }
        }
    }

    @Nested
    @DisplayName("GET: book/genre/{isbn}")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class GetBookGenres {
        @Test
        fun `should return all the genres of a book`() {
            val isbn = "8804780169"
            mockMvc.get("$baseUrl/genre/$isbn")
                .andDo{print()}
                .andExpect{
                    status {isOk()}
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.*"){hasSize<Any>(2)}
                        jsonPath("$[*].name"){containsInAnyOrder("Thriller", "Adventure")}
                        jsonPath("$[*].name"){ not(containsInAnyOrder("Fantasy")) }
                    }
                }
        }
    }
}