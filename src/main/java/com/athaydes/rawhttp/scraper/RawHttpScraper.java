package com.athaydes.rawhttp.scraper;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.client.TcpRawHttpClient;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RawHttpScraper implements AutoCloseable {

    private final RawHttp http = new RawHttp();
    private final TcpRawHttpClient httpClient = new TcpRawHttpClient();

    public void run(InputStream requestStream,
                    String script) throws IOException {
        ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("js");

        RawHttpRequest request = http.parseRequest(requestStream);

        RawHttpResponse<Void> response = httpClient.send(request).eagerly();

        Bindings bindings = new SimpleBindings();
        bindings.put("response", response);
        bindings.put("UTF8", StandardCharsets.UTF_8);

        try {
            scriptEngine.eval(script, bindings);
        } catch (ScriptException e) {
            error("Error: " + e, 10);
        }
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            File file = new File(args[0]);
            File script = new File(args[1]);
            runRequestFromFile(file, script);
        } else if (args.length == 0 || args.length == 1) {
            error("Please provide 2 arguments:\n" +
                    " * the file to read a RawHTTP Scrapper script from\n" +
                    " * the JS script to run with the 'response'", 1);
        } else {
            error("Too many arguments, only 2 expected", 2);
        }
    }

    private static void runRequestFromFile(File request, File script) {
        if (!request.isFile()) {
            error("Not a file: " + request, 3);
        }
        if (!script.isFile()) {
            error("Not a file: " + script, 3);
        }
        try (RawHttpScraper scraper = new RawHttpScraper();
             InputStream requestIS = new FileInputStream(request);
             FileReader scriptReader = new FileReader(script)) {
            String scriptText = readAsText(scriptReader);
            scraper.run(requestIS, scriptText);
        } catch (IOException e) {
            error("Error: " + e, 4);
        }
    }

    private static String readAsText(FileReader reader) throws IOException {
        StringBuilder builder = new StringBuilder(512);
        char[] buffer = new char[256];
        int byteCount;
        while ((byteCount = reader.read(buffer)) > 0) {
            builder.append(buffer, 0, byteCount);
        }
        return builder.toString();
    }

    private static void error(String message, int code) {
        System.err.println(message);
        System.exit(code);
    }

}
