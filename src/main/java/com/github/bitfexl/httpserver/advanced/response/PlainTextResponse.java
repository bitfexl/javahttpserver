package com.github.bitfexl.httpserver.advanced.response;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PlainTextResponse extends Response {
    /**
     * Create a new plain text response.
     * @param code The http status code.
     * @param content The response content.
     * @return The new response.
     */
    public static PlainTextResponse of(int code, String content) {
        PlainTextResponse response = new PlainTextResponse();
        response.setResponseCode(code);
        response.setContent(content);
        return response;
    }

    /**
     * Create a new plain text response.
     * @param content The response content.
     * @return The new response.
     */
    public static PlainTextResponse of(String content) {
        return of(200, content);
    }

    /**
     * The body content to send
     */
    private String content;

    private byte[] contentBytes;

    public PlainTextResponse() {
        setHeader("Content-Type", "text/plain");
    }

    @Override
    protected void sendBody(OutputStream body) throws IOException {
        if(content != null) {
            prepareBody();
            body.write(contentBytes);
        }
        contentBytes = null;
    }

    @Override
    protected Long getBodyLength() {
        prepareBody();
        return (long)contentBytes.length;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private void prepareBody() {
        if(content != null && contentBytes == null) {
            contentBytes = content.getBytes(StandardCharsets.UTF_8);
        }
    }
}
