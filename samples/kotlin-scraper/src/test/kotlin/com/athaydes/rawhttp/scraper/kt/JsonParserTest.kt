package com.athaydes.rawhttp.scraper.kt

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.properties.forAll
import io.kotlintest.properties.headers
import io.kotlintest.properties.row
import io.kotlintest.properties.table
import org.junit.Test

class JsonParserTest {

    @Test
    fun canParseJson0() {
        val json = """
            {
                "event": "Example",
                "date": "today",
                "speakers": []
            }
        """.trimIndent()

        parseJsonAsJavaMeetup(json.toByteArray()).run {
            event shouldBe "Example"
            date shouldBe "today"
            speakers shouldBe listOf<Speaker>()
        }
    }

    @Test
    fun canParseJson1() {
        val json = """
            {
                "event": "Example",
                "date": "today",
                "speakers": [
                    {
                        "name": "Joe",
                        "org": "ACME",
                        "topic": "Kotlin"
                    }
                ]
            }
        """.trimIndent()

        parseJsonAsJavaMeetup(json.toByteArray()).run {
            event shouldBe "Example"
            date shouldBe "today"
            speakers shouldBe listOf(Speaker("Joe", "ACME", "Kotlin"))
        }
    }

    @Test
    fun canParseJson2() {
        val json = """
            {
                "event": "Meetup",
                "speakers": [
                    {
                        "name": "Mary",
                        "org": "ABC",
                        "topic": "Java"
                    },
                    {
                        "name": "Jenny",
                        "org": "DEF",
                        "topic": "Groovy"
                    }
                ],
                "date": "tomorrow"
            }
        """.trimIndent()

        parseJsonAsJavaMeetup(json.toByteArray()).run {
            event shouldBe "Meetup"
            date shouldBe "tomorrow"
            speakers shouldBe listOf(
                    Speaker("Mary", "ABC", "Java"),
                    Speaker("Jenny", "DEF", "Groovy"))
        }
    }

    @Test
    fun cannotParseInvalidJson() {
        val examples = table(headers("JSON value"),
                row("{}"),
                row(""),
                row("""
                { "event": "wrong" }
                """.trimIndent()),
                row("""
                { "event": "wrong", "date": "today" }
                """.trimIndent()),
                row("[1,2,3]"),
                row("""
                "hello"
                """.trimIndent()))

        forAll(examples) { json ->
            shouldThrow<Exception> { parseJsonAsJavaMeetup(json.toByteArray()) }
        }
    }
}