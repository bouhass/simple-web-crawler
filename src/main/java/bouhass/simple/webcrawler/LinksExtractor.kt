package bouhass.simple.webcrawler

import org.jsoup.Jsoup

class LinksExtractor {

    fun extractLinks(url: String): Set<String> {
        val doc = Jsoup.connect(url).get()
        val links = doc.select("a")
        return links.map { link -> link.absUrl("href") }.toSet()
    }

}
