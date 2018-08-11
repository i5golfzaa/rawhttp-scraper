package com.athaydes.rawhttp.scraper;

import java.io.File;
import java.util.Objects;

import static com.athaydes.rawhttp.scraper.Errors.USAGE_ERROR;
import static com.athaydes.rawhttp.scraper.Errors.error;

final class CliOptions {

    private final File requestFile;
    private final String scraperId;
    private final String[] args;

    CliOptions(File requestFile, String scraperId, String... args) {
        this.requestFile = Objects.requireNonNull(requestFile);
        this.scraperId = scraperId == null ? "default" : scraperId;
        this.args = Objects.requireNonNull(args);
    }

    public File getRequestFile() {
        return requestFile;
    }

    public String getScraperId() {
        return scraperId;
    }

    public String[] getScraperArgs() {
        return args;
    }

    static CliOptions parse(String... mainArgs) {
        if (mainArgs.length == 0) {
            error("No arguments provided. Use -h to see usage", USAGE_ERROR);
        }
        if (mainArgs.length == 1 && mainArgs[0].equalsIgnoreCase("-h")) {
            printHelp();
            System.exit(0);
        }

        File requestFile = new File(mainArgs[0]);

        String scraperId = null;
        String[] scraperArgs = new String[0];

        if (mainArgs.length > 1) {
            scraperId = mainArgs[1];
            if (mainArgs.length > 2) {
                scraperArgs = new String[mainArgs.length - 2];
                System.arraycopy(mainArgs, 2, scraperArgs, 0, scraperArgs.length);
            }
        }

        return new CliOptions(requestFile, scraperId, scraperArgs);
    }

    private static void printHelp() {
        System.out.println("========== RawHTTP Scraper ==========\n\n" +
                "Runs a HTTP request from a input file, then either prints the response to stdout\n" +
                "or uses a scraper given by ID to process the response.\n\n" +
                "Usage:\n" +
                "  rawhttp-scraper <request-file> [scraper-id [options]]\n\n" +
                "Examples:\n" +
                "  Run the HTTP request in file req.txt and print the full response.\n" +
                "    rawhttp-scraper req.txt\n" +
                "  Run the HTTP request in file req.txt using 'js' scraper,\n" +
                "  which runs the given parseJson.js script with the 'response'.\n" +
                "    rawhttp-scraper req.txt js parseJson.js");
    }
}
