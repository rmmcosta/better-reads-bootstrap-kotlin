package com.rmmcosta.betterreadskotlinbootstrap.book

import com.rmmcosta.betterreadskotlinbootstrap.author.AuthorRepository
import com.rmmcosta.betterreadskotlinbootstrap.logger
import org.json.JSONObject
import java.time.LocalDate

fun getBookFromWorksDumpLine(worksDumpLine: String?, authorRepository: AuthorRepository): Book? =
    getBookFromJson(bookLineToJsonString(worksDumpLine), authorRepository)

private fun getBookFromJson(bookJson: String?, authorRepository: AuthorRepository): Book? {
    if (bookJson == null) {
        return null
    }

    val bookJsonObject = JSONObject(bookJson)
    val bookKey = bookJsonObject.getString("key").replace("/works/", "")
    val bookTitle = bookJsonObject.getString("title")
    val bookDescription = bookJsonObject.getJSONObject("description")?.getString("value") ?: ""
    val bookPublishedDate = bookJsonObject.getJSONObject("created")?.getString("value") ?: ""
    val bookAuthors = bookJsonObject.getJSONArray("authors")
    val bookCoverIds = bookJsonObject.getJSONArray("covers")
    val bookAuthorIds =
        bookAuthors.map { JSONObject(it.toString()).getJSONObject("author").getString("key").replace("/authors/", "") }
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

private fun bookLineToJsonString(bookLine: String?): String {
    if (bookLine == null) {
        logger.warn("null")
        return ""
    }
    val jsonStartIdx = bookLine.indexOf('{')
    return bookLine.substring(jsonStartIdx)
}