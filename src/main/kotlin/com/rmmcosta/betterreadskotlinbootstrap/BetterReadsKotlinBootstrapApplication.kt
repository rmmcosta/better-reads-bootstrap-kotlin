package com.rmmcosta.betterreadskotlinbootstrap

import com.rmmcosta.betterreadskotlinbootstrap.author.Author
import com.rmmcosta.betterreadskotlinbootstrap.author.AuthorRepository
import com.rmmcosta.betterreadskotlinbootstrap.author.getAuthorFromAuthorsDumpLine
import com.rmmcosta.betterreadskotlinbootstrap.book.Book
import com.rmmcosta.betterreadskotlinbootstrap.book.BookRepository
import com.rmmcosta.betterreadskotlinbootstrap.book.getBookFromWorksDumpLine
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.nio.file.Files
import kotlin.io.path.Path

val logger: Logger = LoggerFactory.getLogger(BetterReadsKotlinBootstrapApplication::class.java)

@SpringBootApplication
class BetterReadsKotlinBootstrapApplication(
    val authorRepository: AuthorRepository,
    val bookRepository: BookRepository,
) {
    @Value("\${data-dump.authors.path}")
    private lateinit var authorsPath: String

    @Value("\${data-dump.books.path}")
    private lateinit var booksPath: String

    var countAuthors = 0
    var countBooks = 0

    fun bootstrap() {
        logger.info("bootstrap data inside bootstrap function")
        bootstrapAuthors()
        bootstrapBooks()
    }

    fun bootstrapAuthors() {
        logger.info("number of authors before: $countAuthors")
        val authorsBuffer = Files.newBufferedReader(Path(authorsPath))
        authorsBuffer.lines().forEach { line -> saveAuthor(getAuthorFromAuthorsDumpLine(line)) }
        logger.info("number of authors after: $countAuthors")
    }

    fun bootstrapBooks() {
        logger.info("number of books before: $countBooks")
        val booksBuffer = Files.newBufferedReader(Path(booksPath))
        booksBuffer.lines().forEach { line -> saveBook(getBookFromWorksDumpLine(line, authorRepository)) }
        logger.info("number of books after: $countBooks")
    }

    private fun saveAuthor(author: Author?) {
        countAuthors++
        logger.info("current count Authors: $countAuthors")
        if (author != null) authorRepository.save(author)
    }

    private fun saveBook(book: Book?) {
        countBooks++
        logger.info("current count Books: $countBooks")
        if (book != null) bookRepository.save(book)
    }
}

fun main(args: Array<String>) {
    val context = runApplication<BetterReadsKotlinBootstrapApplication>(*args)
    val mainClass = context.getBean(BetterReadsKotlinBootstrapApplication::class.java)
    mainClass.bootstrap()
}
