package com.github.bitfexl.httpserver.advanced.response;

public class HTMLResponse extends PlainTextResponse {
    public HTMLResponse() {
        setHeader("Content-Type", "text/html");
    }
}
