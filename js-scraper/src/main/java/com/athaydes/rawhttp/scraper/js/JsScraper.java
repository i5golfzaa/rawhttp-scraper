package com.athaydes.rawhttp.scraper.js;

import com.athaydes.rawhttp.scraper.spi.InvalidOptionsException;
import com.athaydes.rawhttp.scraper.spi.Scraper;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import rawhttp.core.RawHttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.util.stream.Collectors.joining;

public final class JsScraper implements Scraper<JsOptions> {
    @Override
    public String getId() {
        return "js";
    }

    @Override
    public JsOptions parseArgs(String... args) throws InvalidOptionsException {
        if (args.length == 1) {
            File jsFile = new File(args[0]);
            if (jsFile.isFile()) {
                return new JsOptions(jsFile);
            } else {
                throw new InvalidOptionsException("Not a file: " + jsFile);
            }
        }
        throw new InvalidOptionsException("JS scraper expects a JS file as an argument");
    }

    @Override
    public void accept(RawHttpResponse<?> response,
                       JsOptions jsOptions) throws IOException {
        try (Context context = Context.create("js")) {
            Value bindings = context.getBindings("js");
            bindings.putMember("response", JSUtils.proxyFor(response));
            context.eval("js", Files.lines(jsOptions.getScript())
                    .collect(joining("\n")));
        }
    }
}

