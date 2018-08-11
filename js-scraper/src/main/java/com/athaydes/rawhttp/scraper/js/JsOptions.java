package com.athaydes.rawhttp.scraper.js;

import java.io.File;
import java.nio.file.Path;

final class JsOptions {
    private final File script;

    JsOptions(File script) {
        this.script = script;
    }

    Path getScript() {
        return script.toPath();
    }
}
