package com.github.bitfexl.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;

public class HttpServer implements HttpHandler {
    private com.sun.net.httpserver.HttpServer httpServer;

    /**
     * The print stream to use for debug messages.
     * Can be null.
     * Default: System.out
     */
    private PrintStream printStream;

    private RequestHandlerList handlers;

    /**
     * Init a new http server with print stream set to stdout.
     * @throws IOException Error creating http server.
     */
    public HttpServer() throws IOException {
        httpServer = com.sun.net.httpserver.HttpServer.create();
        httpServer.createContext("/", this);
        this.printStream = System.out;
        this.handlers = new RequestHandlerList();
    }

    /**
     * Start the server.
     * @param port The port to run on.
     * @return this
     * @throws IOException Error binding address.
     */
    public HttpServer start(int port) throws IOException {
        return start(port, 0);
    }

    /**
     * Start the server.
     * @param port The port to run on.
     * @param backlog The max backlog.
     * @return this
     * @throws IOException Error binding address.
     */
    public HttpServer start(int port, int backlog) throws IOException {
        httpServer.bind(new InetSocketAddress("localhost", port), backlog);
        httpServer.start();
        printMsg("Running on http://localhost:" + port + "/");
        return this;
    }

    public void stop() {
        httpServer.stop(1);
        System.out.println("Server stopped!");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        printMsg("New request: " + exchange.getRequestMethod() + " " + exchange.getRequestURI().getPath());

        Request request = new Request(Method.fromString(exchange.getRequestMethod()), exchange.getRequestMethod(), exchange.getRequestHeaders(), exchange.getRequestURI().getPath()) {
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

        RequestHandler handler = handlers.getHandler(request.getPath());

        if(handler != null) {
            handler.handleRequest(request);
        }

        exchange.close();

        // System.out.println(URLDecoder.decode("asdf%20asdf", StandardCharsets.UTF_8));
    }

    public void setPrintStream(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void setDefaultHandler(RequestHandler defaultHandler) {
        handlers.setDefaultHandler(defaultHandler);
    }

    /**
     * Register a handler.
     * @param path The path. Starting and ending with "/" (added if missing). Can have a trailing "*" for all sub paths.
     * @param handler The handler to register.
     */
    public void setHandler(String path, RequestHandler handler) {
        handlers.setHandler(path, handler);
    }

    private void printMsg(String msg) {
        if(printStream != null) {
            printStream.println(msg);
        }
    }
}
