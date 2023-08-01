package com.rmmcosta.betterreadskotlinbootstrap

import com.rmmcosta.betterreadskotlinbootstrap.author.Author
import com.rmmcosta.betterreadskotlinbootstrap.author.AuthorRepository
import org.json.JSONObject
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.nio.file.Files
import kotlin.io.path.Path

const val FILE_AUTHORS_PATH = "src/main/resources/test-authors.txt"

@SpringBootApplication
class BetterReadsKotlinBootstrapApplication(
    val authorRepository: AuthorRepository
) {
    var countAuthors = 0
    fun bootstrap() {
        //println("bootstrap data inside bootstrap function")
        bootstrapAuthors()
        //bootstrapBooks()
    }

    fun bootstrapBooks() {
        TODO("Not yet implemented")
    }

    fun bootstrapAuthors() {
        println("number of authors before: $countAuthors")
        val authorsBuffer = Files.newBufferedReader(Path(FILE_AUTHORS_PATH))
        authorsBuffer.lines().forEach { line -> saveAuthor(getAuthorFromJson(authorLineToJsonString(line))) }
        println("number of authors after: $countAuthors")
    }

    fun authorLineToJsonString(authorLine: String?): String? {
        if (authorLine == null) {
            println("null")
            return null
        }
        val jsonStartIdx = authorLine.indexOf('{')

        val authorJsonString = authorLine.substring(jsonStartIdx)
        println(authorJsonString)
        return authorJsonString
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

    fun saveAuthor(author: Author?) {
        countAuthors++
        println(countAuthors)
        if (author != null) authorRepository.save(author)
    }
}

fun main(args: Array<String>) {
    val context = runApplication<BetterReadsKotlinBootstrapApplication>(*args)
    val mainClass = context.getBean(BetterReadsKotlinBootstrapApplication::class.java)
    mainClass.bootstrap()
}
