package com.athaydes.rawhttp.scraper.kt

import com.athaydes.rawhttp.scraper.spi.Scraper
import rawhttp.core.RawHttpResponse

class KotlinScraper : Scraper<Unit> {

    override fun getId() = "kotlin"

    override fun parseArgs(vararg args: String) {
        if (args.isNotEmpty()) {
            System.err.println("Ignoring options: $args")
        }
    }

    override fun accept(response: RawHttpResponse<*>, options: Unit) {
        if (response.statusCode != 200) {
            throw Exception("Unexpected status code: ${response.statusCode}")
        }
        response.body.map { bodyReader ->
            val meetupInfo = parseJsonAsJavaMeetup(bodyReader.decodeBody())
            show(meetupInfo)
        }.orElseThrow { Exception("No body provided") }
    }

    private fun show(info: JavaMeetup) {
        println("Welcome to the ${info.event}")
        println("Number of speakers: ${info.speakers.size}")
        info.speakers.forEach { speaker ->
            println("${speaker.name} from ${speaker.org} talks about ${speaker.topic}")
        }
    }

}

data class JavaMeetup(
        val event: String,
        val date: String,
        val speakers: List<Speaker>
)

data class Speaker(
        val name: String,
        val org: String,
        val topic: String
)