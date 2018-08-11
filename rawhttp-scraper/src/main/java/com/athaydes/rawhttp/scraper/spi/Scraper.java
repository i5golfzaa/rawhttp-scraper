package com.athaydes.rawhttp.scraper.spi;

import rawhttp.core.RawHttpResponse;

public interface Scraper<Options> {

    String getId();

    Options parseArgs(String... args) throws InvalidOptionsException;

    void accept(RawHttpResponse<?> response, Options options) throws Exception;

}
