package com.athaydes.rawhttp.scraper;

import com.athaydes.rawhttp.scraper.spi.InvalidOptionsException;
import com.athaydes.rawhttp.scraper.spi.Scraper;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.client.TcpRawHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.athaydes.rawhttp.scraper.Errors.GENERAL_ERROR;
import static com.athaydes.rawhttp.scraper.Errors.IO_ERROR;
import static com.athaydes.rawhttp.scraper.Errors.USAGE_ERROR;
import static com.athaydes.rawhttp.scraper.Errors.error;

public final class Main implements AutoCloseable {

    private final RawHttp http = new RawHttp();
    private final TcpRawHttpClient httpClient = new TcpRawHttpClient(null, http);
    private final ScraperFinder scraperFinder = new ScraperFinder();

    private void run(CliOptions options) throws Exception {
        String scraperId = options.getScraperId();
        Optional<Scraper<?>> scraper = scraperFinder.findScraper(scraperId);
        if (scraper.isPresent()) {
            scrape(scraper.get(), options.getRequestFile(), options.getScraperArgs());
        } else {
            error("Scraper with ID=" + scraperId + " was not found", USAGE_ERROR);
        }
    }

    private <T> void scrape(Scraper<T> scraper, File requestFile, String... scraperArgs)
            throws Exception {
        T options = scraper.parseArgs(scraperArgs);
        RawHttpRequest request = http.parseRequest(requestFile);
        RawHttpResponse<?> response = httpClient.send(request);
        scraper.accept(response, options);
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

    public static void main(String[] args) {
        CliOptions options = CliOptions.parse(args);
        try (Main main = new Main()) {
            main.run(options);
        } catch (IOException e) {
            error("IO Error: " + e, IO_ERROR);
        } catch (InvalidOptionsException e) {
            error("Scraper options error: " + e.getMessage(), USAGE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            error("Unexpected error", GENERAL_ERROR);
        }
    }

}
