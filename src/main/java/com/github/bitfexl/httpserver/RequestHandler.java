package com.github.bitfexl.httpserver;

import java.io.IOException;

public interface RequestHandler {
    /**
     * Handle a given request.
     * @param request The request to handle.
     */
    void handleRequest(Request request) throws IOException;
}
