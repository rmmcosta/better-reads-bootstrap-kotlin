package com.rmmcosta.betterreadskotlinbootstrap.book

import com.rmmcosta.betterreadskotlinbootstrap.author.Author
import com.rmmcosta.betterreadskotlinbootstrap.author.AuthorRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Stream

class BookTransformationsKtTest {

    private val authorRepository = mockk<AuthorRepository>()

    init {
        every { authorRepository.findById(any()) } returns Optional.of(Author("1", "Author 1", "Author 1"))
    }

    companion object {
        @JvmStatic
        fun readLinesFromFile(): Stream<String> {
            val path = Paths.get(ClassLoader.getSystemResource("test-works.txt").toURI())
            return Files.lines(path)
        }
    }

    @ParameterizedTest
    @MethodSource("readLinesFromFile")
    fun `getBookFromBooksDumpLine should return a book for all correct dump lines`(line: String) {
        val result = getBookFromWorksDumpLine(line, authorRepository = authorRepository)
        assertNotNull(result)
    }
}