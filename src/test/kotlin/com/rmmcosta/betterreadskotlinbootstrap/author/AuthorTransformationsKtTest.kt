package com.rmmcosta.betterreadskotlinbootstrap.author

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

class AuthorTransformationsKtTest {

    companion object {
        @JvmStatic
        fun readLinesFromFile(): Stream<String> {
            val path = Paths.get(ClassLoader.getSystemResource("test-authors.txt").toURI())
            return Files.lines(path)
        }
    }

    @ParameterizedTest
    @MethodSource("readLinesFromFile")
    fun `getAuthorFromAuthorsDumpLine should return an author for all correct dump lines`(line: String) {
        val result = getAuthorFromAuthorsDumpLine(line)
        assertNotNull(result)
    }
}