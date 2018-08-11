package com.athaydes.rawhttp.scraper.spi;

import rawhttp.core.RawHttpResponse;

public interface Scraper {

    String getId();

    void accept(RawHttpResponse<?> response, String... args);

}
