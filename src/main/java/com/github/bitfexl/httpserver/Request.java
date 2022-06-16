package com.github.bitfexl.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public abstract class Request {
    /**
     * Get a request object for a given http exchange.
     * @param exchange The exchange to handle.
     * @return A request object handling the exchange. exchange.close() should probably be called after handling.
     */
    public static Request forExchange(HttpExchange exchange) {
        return new Request(exchange.getRequestMethod(), exchange.getRequestHeaders(), exchange.getRequestURI().getPath(), parseParameters(exchange.getRequestURI().getRawQuery())) {
            @Override
            public void setHeader(String name, String value) {
                exchange.getResponseHeaders().set(name, value);
            }

            @Override
            public InputStream getRequestBody() {
                return exchange.getRequestBody();
            }

            @Override
            public OutputStream beginBody(int code) throws IOException {
                exchange.sendResponseHeaders(code, 0);
                return exchange.getResponseBody();
            }
        };
    }

    private static HashMap<String, String> parseParameters(String rawParameters) {
        HashMap<String, String> parameters = new HashMap<>();
        if(rawParameters == null) {
            return parameters;
        }

        for(String parameter : rawParameters.split("\\?")) {
            String[] parts = parameter.split("=");
            if(parts.length > 1) {
                parameters.put(parts[0], URLDecoder.decode(parts[1], StandardCharsets.UTF_8));
            } else {
                parameters.put(parts[0], "");
            }
        }

        return parameters;
    }

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

    /**
     * Http GET parameters.
     */
    private HashMap<String, String> parameters;

    public Request(String rawMethod, Headers headers, String path, HashMap<String, String> parameters) {
        this.method = Method.fromString(rawMethod);
        this.rawMethod = rawMethod;
        this.headers = headers;
        this.path = path;
        this.parameters = parameters;
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

    public HashMap<String, String> getParameters() {
        return parameters;
    }
}
