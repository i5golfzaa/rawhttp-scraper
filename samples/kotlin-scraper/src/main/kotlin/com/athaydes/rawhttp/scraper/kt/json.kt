package com.athaydes.rawhttp.scraper.kt

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

internal fun parseJsonAsJavaMeetup(json: ByteArray): JavaMeetup {
    val jsonParser = JsonFactory().createParser(json)
    return jsonParser.parseJsonAsJavaMeetup()
}

private fun JsonParser.parseJsonAsJavaMeetup(): JavaMeetup {
    if (nextToken() != JsonToken.START_OBJECT) {
        throw Exception("Invalid JSON: not an object: $currentToken")
    }

    var event: String? = null
    var date: String? = null
    var speakers: List<Speaker>? = null

    while (nextToken() != JsonToken.END_OBJECT) {
        val fieldName = currentName
        nextToken()
        when (fieldName) {
            "event" -> event = valueAsString
            "date" -> date = valueAsString
            "speakers" -> speakers = parseSpeakers()
            else -> throw Exception("Unexpected field: $fieldName")
        }
    }

    return JavaMeetup(
            event.ensureFieldIsPresent("event"),
            date.ensureFieldIsPresent("date"),
            speakers.ensureFieldIsPresent("speakers"))
}

private fun JsonParser.parseSpeakers(): List<Speaker> {
    if (!isExpectedStartArrayToken) {
        throw Exception("speakers expected to be an array, but found $currentToken")
    }
    val speakers = mutableListOf<Speaker>()
    while (nextToken() != JsonToken.END_ARRAY) {
        speakers += parseSpeaker()
    }
    return speakers
}

private fun JsonParser.parseSpeaker(): Speaker {
    if (!isExpectedStartObjectToken) {
        throw Exception("Invalid JSON: not an object: $currentToken")
    }

    var name: String? = null
    var org: String? = null
    var topic: String? = null

    while (nextToken() != JsonToken.END_OBJECT) {
        val fieldName = currentName
        nextToken()
        when (fieldName) {
            "name" -> name = valueAsString
            "org" -> org = valueAsString
            "topic" -> topic = valueAsString
            else -> throw Exception("Unexpected field: $fieldName")
        }
    }

    return Speaker(
            name.ensureFieldIsPresent("name"),
            org.ensureFieldIsPresent("org"),
            topic.ensureFieldIsPresent("topic"))
}

private fun <T> T?.ensureFieldIsPresent(name: String): T {
    if (this == null) {
        throw Exception("$name was not present in the JSON object")
    }
    return this
}