package com.rmmcosta.betterreadskotlinbootstrap

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rmmcosta.betterreadskotlinbootstrap.author.Author
import com.rmmcosta.betterreadskotlinbootstrap.author.AuthorRepository
import com.rmmcosta.betterreadskotlinbootstrap.book.Book
import com.rmmcosta.betterreadskotlinbootstrap.book.BookRepository
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.nio.file.Files
import java.time.LocalDate
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

    fun bootstrapBooks() {
        logger.info("number of books before: $countBooks")
        val booksBuffer = Files.newBufferedReader(Path(booksPath))
        booksBuffer.lines().forEach { line -> saveBook(getBookFromJson(bookLineToJsonString(line))) }
        logger.info("number of books after: $countBooks")
    }

    fun bootstrapAuthors() {
        logger.info("number of authors before: $countAuthors")
        val authorsBuffer = Files.newBufferedReader(Path(authorsPath))
        authorsBuffer.lines().forEach { line -> saveAuthor(getAuthorFromJson(authorLineToJsonString(line))) }
        logger.info("number of authors after: $countAuthors")
    }

    fun authorLineToJsonString(authorLine: String?): String? {
        if (authorLine == null) {
            logger.warn("null")
            return null
        }
        val jsonStartIdx = authorLine.indexOf('{')

        val authorJsonString = authorLine.substring(jsonStartIdx)
        logger.info(authorJsonString)
        return authorJsonString
    }

    private fun bookLineToJsonString(bookLine: String?): String {
        if (bookLine == null) {
            logger.warn("null")
            return ""
        }
        val jsonStartIdx = bookLine.indexOf('{')
        return bookLine.substring(jsonStartIdx)
    }

    fun getAuthorFromJson(authorJson: String?): Author? {
        if (authorJson == null) {
            return null
        }

        val authorJsonObject = JSONObject(authorJson)
        val authorKey = authorJsonObject.getString("key").replace("/authors/", "")
        val authorName = authorJsonObject.getString("name")
        val authorPersonalName =
            if (authorJsonObject.has("personal_name")) authorJsonObject.getString("personal_name") else authorName

        return Author(authorKey, authorName, authorPersonalName)
    }

    private fun getBookFromJson(bookLineToJsonString: String?): Book? {
        if (bookLineToJsonString == null) {
            return null
        }

        val bookJsonObject = JSONObject(bookLineToJsonString)
        val bookKey = bookJsonObject.getString("key").replace("/works/", "")
        val bookTitle = bookJsonObject.getString("title")
        val bookDescription = bookJsonObject.getJSONObject("description")?.getString("value") ?: ""
        val bookPublishedDate = bookJsonObject.getJSONObject("created")?.getString("value") ?: ""
        val bookAuthors = bookJsonObject.getJSONArray("authors")
        val bookCoverIds = bookJsonObject.getJSONArray("covers")
        val bookAuthorIds = bookAuthors.map { JSONObject(it.toString()).getJSONObject("author").getString("key").replace("/authors/","") }
        val bookAuthorNames = bookAuthorIds.map { authorRepository.findById(it).get().name }

        return Book(
            bookKey,
            bookTitle,
            bookDescription,
            LocalDate.parse(bookPublishedDate),
            bookCoverIds.toList().map { it.toString().toLong() },
            bookAuthorNames,
            bookAuthorIds,
        )
    }

    fun saveAuthor(author: Author?) {
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
