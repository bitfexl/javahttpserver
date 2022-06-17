package com.github.bitfexl.httpserver.advanced.response;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PlainTextResponse extends Response {
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
