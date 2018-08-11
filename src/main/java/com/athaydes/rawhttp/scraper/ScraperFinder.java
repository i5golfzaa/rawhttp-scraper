package com.athaydes.rawhttp.scraper;

import com.athaydes.rawhttp.scraper.spi.Scraper;

import java.util.Optional;

final class ScraperFinder {

    Optional<Scraper> findScraper(String scraperId) {
        if ("default".equalsIgnoreCase(scraperId)) {
            return Optional.of(new DefaultScraper());
        }
        // TODO use ServiceLoader to get a scraper
        return Optional.empty();
    }

}
