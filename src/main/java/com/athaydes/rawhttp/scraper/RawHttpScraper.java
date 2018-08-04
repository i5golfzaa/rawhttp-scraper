package com.athaydes.rawhttp.scraper;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.client.TcpRawHttpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RawHttpScraper implements AutoCloseable {

    private final RawHttp http = new RawHttp();
    private final TcpRawHttpClient httpClient = new TcpRawHttpClient();

    public void run(InputStream inputStream) throws IOException {
        RawHttpRequest request = http.parseRequest(inputStream);
        System.out.println(request);
        RawHttpResponse<Void> response = httpClient.send(request).eagerly();
        System.out.println("-- response --");
        System.out.println(response);
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            File file = new File(args[0]);
            runRequestFromFile(file);
        } else if (args.length == 0) {
            error("Please provide the file to read a RawHTTP Scrapper script from", 1);
        } else {
            error("Too many arguments. Only one expected: file to read a RawHTTP Scrapper script from", 2);
        }
    }

    private static void runRequestFromFile(File file) {
        if (file.isFile()) {
            try (RawHttpScraper scraper = new RawHttpScraper();
                 InputStream is = new FileInputStream(file)) {
                scraper.run(is);
            } catch (IOException e) {
                error("Error: " + e, 4);
            }
        } else {
            error("Not a file: " + file, 3);
        }
    }

    private static void error(String s, int i) {
        System.err.println(s);
        System.exit(i);
    }

}
