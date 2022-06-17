package com.github.bitfexl.httpserver.advanced;

import com.github.bitfexl.httpserver.advanced.response.Response;

public interface AdvancedRequestHandler {
    Response handleRequest();
}
