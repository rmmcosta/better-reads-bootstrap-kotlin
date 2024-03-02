package com.rmmcosta.betterreadskotlinbootstrap.author

import com.rmmcosta.betterreadskotlinbootstrap.logger
import org.json.JSONObject

fun getAuthorFromAuthorsDumpLine(authorsDumpLine: String?): Author? =
    getAuthorFromJson(authorLineToJsonString(authorsDumpLine))

private fun getAuthorFromJson(authorJson: String?): Author? {
    if (authorJson == null) {
        return null
    }

    val authorJsonObject = JSONObject(authorJson)
    val authorKey = authorJsonObject.getString("key").replace("/authors/", "")
    val authorName = authorJsonObject.optString("name") ?: "unknown"
    val authorPersonalName =
        if (authorJsonObject.has("personal_name")) authorJsonObject.getString("personal_name") else authorName

    return Author(authorKey, authorName, authorPersonalName)
}

private fun authorLineToJsonString(authorLine: String?): String? {
    if (authorLine == null) {
        logger.warn("null")
        return null
    }
    val jsonStartIdx = authorLine.indexOf('{')

    val authorJsonString = authorLine.substring(jsonStartIdx)
    logger.info(authorJsonString)
    return authorJsonString
}