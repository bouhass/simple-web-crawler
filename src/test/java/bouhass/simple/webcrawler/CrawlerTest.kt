package bouhass.simple.webcrawler

import io.mockk.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CrawlerTest {

    val seedUrl = "https://www.acme.com"
    val linksExtractor = mockk<LinksExtractor>()
    val crawler = spyk(Crawler(seedUrl, linksExtractor))

    @Test
    fun `test visited links should be empty by default`() {
        assertTrue(crawler.visited.isEmpty())
    }

    @Test
    fun `test crawl`() {
        every { linksExtractor.extractLinks(seedUrl) } returns setOf("$seedUrl/Page1", "$seedUrl/Page2")
        every { linksExtractor.extractLinks("$seedUrl/Page1") } returns setOf()
        every { linksExtractor.extractLinks("$seedUrl/Page2") } returns setOf()

        crawler.start()

        assertEquals(setOf("$seedUrl/Page1", "$seedUrl/Page2"), crawler.visited)
        verify { crawler.visit("$seedUrl/Page1") }
        verify { crawler.visit("$seedUrl/Page2") }
    }

    @Test
    fun `test should not visit an already visited link`() {
        crawler.visited.add("$seedUrl/Page1")
        every { linksExtractor.extractLinks(seedUrl) } returns setOf("$seedUrl/Page1", "$seedUrl/Page2")
        every { linksExtractor.extractLinks("$seedUrl/Page2") } returns setOf()

        crawler.start()

        assertEquals(setOf("$seedUrl/Page1", "$seedUrl/Page2"), crawler.visited)
        verify(inverse = true) { crawler.visit("$seedUrl/Page1") }
        verify { crawler.visit("$seedUrl/Page2") }
    }

    @Test
    fun `test should only visit links within the same sub domain`() {
        every { linksExtractor.extractLinks(seedUrl) } returns setOf("$seedUrl/Page1", "https://www.anotherdomain.com?p=www.acme.com")
        every { linksExtractor.extractLinks("$seedUrl/Page1") } returns setOf()

        crawler.start()

        assertEquals(setOf("$seedUrl/Page1"), crawler.visited)
        verify { crawler.visit("$seedUrl/Page1") }
        verify(inverse = true) { crawler.visit("https://www.anotherdomain.com?p=www.acme.com") }
    }

    @Test
    fun `test should not fail if some links are malformatted`() {
        every { linksExtractor.extractLinks(seedUrl) } returns setOf("$seedUrl/Page1", "")
        every { linksExtractor.extractLinks("$seedUrl/Page1") } returns setOf()

        crawler.start()

        assertEquals(setOf("$seedUrl/Page1"), crawler.visited)
        verify { crawler.visit("$seedUrl/Page1") }
        verify(inverse = true) { crawler.visit("") }
    }

}
