package com.athaydes.rawhttp.scraper;

import org.graalvm.polyglot.proxy.ProxyObject;
import rawhttp.core.RawHttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

final class JSUtils {

    static ProxyObject proxyFor(RawHttpResponse<?> response) {
        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("statusCode", response.getStatusCode());
        propertiesMap.put("startLine", response.getStartLine());
        propertiesMap.put("headers", response.getHeaders());
        propertiesMap.put("body", response.getBody()
                .map(b -> {
                    try {
                        return b.decodeBodyToString(UTF_8);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).orElse(null));
        return ProxyObject.fromMap(propertiesMap);
    }

}
