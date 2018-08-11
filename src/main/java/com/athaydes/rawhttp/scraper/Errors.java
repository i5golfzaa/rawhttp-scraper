package com.athaydes.rawhttp.scraper;

public enum Errors {
    USAGE_ERROR,
    IO_ERROR,
    GENERAL_ERROR;

    public static void error(String message, Errors error) {
        System.err.println(message);
        System.exit(error.ordinal() + 1);
    }
}
