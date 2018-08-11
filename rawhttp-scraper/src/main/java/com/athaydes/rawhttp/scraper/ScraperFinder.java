package com.athaydes.rawhttp.scraper;

import com.athaydes.rawhttp.scraper.spi.Scraper;

import java.util.Optional;
import java.util.ServiceLoader;

final class ScraperFinder {

    Optional<Scraper> findScraper(String scraperId) {
        ServiceLoader<Scraper> scrapers = ServiceLoader.load(Scraper.class);
        for (Scraper scraper : scrapers) {
            if (scraper.getId().equalsIgnoreCase(scraperId)) {
                return Optional.of(scraper);
            }
        }
        return Optional.empty();
    }

}
