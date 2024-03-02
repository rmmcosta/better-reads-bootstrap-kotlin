package com.rmmcosta.betterreadskotlinbootstrap.book

import com.rmmcosta.betterreadskotlinbootstrap.author.AuthorRepository
import com.rmmcosta.betterreadskotlinbootstrap.logger
import org.json.JSONObject
import java.time.LocalDateTime

fun getBookFromWorksDumpLine(worksDumpLine: String?, authorRepository: AuthorRepository): Book? =
    getBookFromJson(bookLineToJsonString(worksDumpLine), authorRepository)

private fun getBookFromJson(bookJson: String?, authorRepository: AuthorRepository): Book? {
    if (bookJson == null) {
        return null
    }

    val bookJsonObject = JSONObject(bookJson)
    val bookKey = bookJsonObject.getString("key").replace("/works/", "")
    val bookTitle = bookJsonObject.getString("title")
    val bookDescription = bookJsonObject.optJSONObject("description")?.getString("value") ?: ""
    val bookPublishedDate = bookJsonObject.optJSONObject("created")?.getString("value") ?: ""
    val bookAuthors = bookJsonObject.getJSONArray("authors")
    val bookCoverIds = bookJsonObject.optJSONArray("covers")
    val bookAuthorIds =
        bookAuthors.map { JSONObject(it.toString()).getJSONObject("author").getString("key").replace("/authors/", "") }
    val bookAuthorNames = bookAuthorIds.map { getAuthorNameFromId(it, authorRepository) }

    return Book(
        bookKey,
        bookTitle,
        bookDescription,
        LocalDateTime.parse(bookPublishedDate).toLocalDate(),
        bookCoverIds?.toList()?.map { it.toString().toLong() } ?: emptyList(),
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

private fun getAuthorNameFromId(authorId: String, authorRepository: AuthorRepository): String {
    val author = authorRepository.findById(authorId)
    return if (author.isPresent) author.get().name ?: "unknown" else "unknown"
}