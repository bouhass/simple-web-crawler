package bouhass.simple.webcrawler

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class LinksExtractorTest {

    val linksExtractor = LinksExtractor()

    @Test
    @Tag("IntegrationTest")
    fun testExtractLinks() {
        val links = linksExtractor.extractLinks("https://en.wikipedia.org/")
        assertTrue(links.contains("https://en.wikipedia.org/wiki/Wikipedia"))
        assertTrue(links.contains("https://en.wikipedia.org/wiki/Free_content"))
        assertTrue(links.contains("https://en.wikipedia.org/wiki/Encyclopedia"))
    }

}
