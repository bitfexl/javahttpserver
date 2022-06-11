package com.github.bitfexl.httpserver;

import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Request {
    /**
     * The request method. Use rawMethod if UNKNOWN.
     */
    private Method method;

    /**
     * The raw request method as a string.
     */
    private String rawMethod;

    /**
     * The request headers.
     */
    private Headers headers;

    /**
     * The request path.
     */
    private String path;

    // todo: get parameters

    public Request(Method method, String rawMethod, Headers headers, String path) {
        this.method = method;
        this.rawMethod = rawMethod;
        this.headers = headers;
        this.path = path;
    }

    /**
     * Set a response header.
     * @param name The name of the header.
     * @param value The new value of the header.
     */
    public abstract void setHeader(String name, String value);

    /**
     * Get the input stream for the request body.
     * @return An input stream from wich the body can be read.
     */
    public abstract InputStream getRequestBody();

    /**
     * The output stream to write the response to.
     * Sends the headers and begins the body -> no headers can be set after calling.
     * Must be closed to terminate the exchange.
     * @param code The http response code.
     * @return Output stream for writing the body.
     */
    public abstract OutputStream beginBody(int code) throws IOException;

    public Method getMethod() {
        return method;
    }

    public String getRawMethod() {
        return rawMethod;
    }

    public Headers getHeaders() {
        return headers;
    }

    public String getPath() {
        return path;
    }
}
