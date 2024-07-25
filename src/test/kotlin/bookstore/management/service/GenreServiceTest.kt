package bookstore.management.service

import bookstore.management.repository.GenreRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class GenreServiceTest {
    private val genreRepository = mockk<GenreRepository>(relaxUnitFun = true)
    private val genreService = GenreService(genreRepository)

    @Test
    fun `should call its data source to retrieve authors`() {
        //given
        every { genreRepository.findAll() } returns emptyList()

        // when
        genreService.getAllGenres()
        // then
        verify(exactly = 1) {genreRepository.findAll()}
    }
}