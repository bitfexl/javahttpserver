package com.github.bitfexl.httpserver.advanced.response;

import com.github.bitfexl.httpserver.simple.Request;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public abstract class Response {
    private HashMap<String, String> headers;

    /**
     * The http response code.
     * Some responses may automatically set the response code.
     * Default: 200 (ok)
     */
    private int responseCode;

    public Response() {
        this.headers = new HashMap<>();
        this.responseCode = 200;
    }

    /**
     * Reply to the given request. Request does NOT get closed after this call.
     * @param request The request to reply to.
     * @throws IOException Something went wrong sending response.
     */
    public void replyTo(Request request) throws IOException {
        for(String header : headers.keySet()) {
            request.setHeader(header, headers.get(header));
        }
        Long bodyLength = getBodyLength();
        bodyLength = bodyLength != null ? bodyLength : 0;
        OutputStream responseBody = request.beginBody(getResponseCode(), bodyLength);
        sendBody(responseBody);
    }

    /**
     * Set a header.
     * @param name The name of the header to set.
     * @param value The value to set.
     * @throws UnsupportedOperationException Header is automatically set by response and cannot be modified.
     */
    public void setHeader(String name, String value) throws UnsupportedOperationException {
        if(value == null) {
            headers.remove(name);
        } else {
            headers.put(name, value);
        }
    }

    public int getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the response code.
     * @param responseCode The http response code to set.
     * @throws UnsupportedOperationException May be thrown by some responses where the response code is automatically set.
     * @throws IllegalArgumentException May be thrown by some responses when the code does not match the response. E.g. something else than 3xx for redirects.
     */
    public void setResponseCode(int responseCode) throws UnsupportedOperationException, IllegalArgumentException {
        this.responseCode = responseCode;
    }

    /**
     * Write the body. Headers and response code have already been transmitted.
     * @param body The input stream to write the body to.
     */
    protected abstract void sendBody(OutputStream body) throws IOException;

    /**
     * Gets called right before sending the body (sendBody() call).
     * @return The length of the body to send (if known) else null.
     */
    protected abstract Long getBodyLength();
}
