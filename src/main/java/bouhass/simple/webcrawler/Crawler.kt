package bouhass.simple.webcrawler

import java.net.URL

class Crawler(val seedUrl: String,
              var linksExtractor: LinksExtractor = LinksExtractor()) {

    val visited = mutableSetOf<String>()
    var baseDomain: String? = null

    fun start() {
        baseDomain = extractDomain(seedUrl)
        crawl(seedUrl)
    }

    fun crawl(url: String) {
        println("crawling url $url")
        val links = try {
            linksExtractor.extractLinks(url)
        } catch (e: Exception) {
            println("failed to extract links from url $url: $e")
            emptySet()
        }

        for (link in links) {
            println("\textracted link $link")
            try {
                if (needToVisit(link)) {
                    visit(link)
                }
            } catch (e: Exception) {
                println("failed to crawl url '$link': $e")
            }
        }
    }

    private fun extractDomain(url: String): String {
        return URL(url).host
    }

    private fun needToVisit(link: String): Boolean {
        val linkDomain = extractDomain(link)
        return linkDomain == baseDomain && !visited.contains(link)
    }

    fun visit(link: String) {
        visited.add(link)
        crawl(link)
    }

}
