package com.athaydes.rawhttp.scraper;

import com.athaydes.rawhttp.scraper.spi.Scraper;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.athaydes.rawhttp.scraper.Errors.IO_ERROR;
import static com.athaydes.rawhttp.scraper.Errors.error;

public final class DefaultScraper implements Scraper {

    @Override
    public String getId() {
        return "default";
    }

    @Override
    public void accept(RawHttpResponse<?> response, String... args) {
        if (args.length != 0) {
            System.err.println("Ignoring options: " + Arrays.toString(args));
        }
        System.out.println(response.getStartLine());
        System.out.println(response.getHeaders());
        response.getBody().ifPresent(bodyReader -> {
            try {
                System.out.println(bodyReader.decodeBodyToString(StandardCharsets.UTF_8));
            } catch (IOException e) {
                error("Unable to decode body using UTF-8", IO_ERROR);
            }
        });
    }

}
