package com.github.bitfexl.httpserver.example;

import com.github.bitfexl.httpserver.advanced.AdvancedHttpServer;
import com.github.bitfexl.httpserver.advanced.annotations.Path;
import com.github.bitfexl.httpserver.advanced.response.PlainTextResponse;
import com.github.bitfexl.httpserver.example.endpoints.LoggingHandler;
import com.github.bitfexl.httpserver.example.endpoints.PersonHandler;
import com.github.bitfexl.httpserver.simple.Request;
import com.github.bitfexl.httpserver.simple.RequestHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

@Path(path="/")
public class Application implements RequestHandler {
    public static void main(String[] args) {
        try {
            new Application().run();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void run() throws Exception {
        AdvancedHttpServer server = new AdvancedHttpServer();

        // default (not found) handler
        server.setDefaultHandler(this::defaultHandler);

        // simple version
        server.setHandler("/", this);

        // advanced version
        server.addHandler(new PersonHandler());
        server.addHandler(new LoggingHandler());

        server.start(65500);
        // server.stop();
    }

    /// "simple" version ///
    @Override
    public void handleRequest(Request request) throws IOException {
        System.out.println("Handling " + request.getPath());

        for(String key : request.getParameters().keySet()) {
            System.out.println(key + "=" + request.getParameters().get(key));
        }

        int responseCode = 200;
        if("google".equalsIgnoreCase(request.getParameters().get("redirect"))) {
            request.setHeader("Location", "https://google.com/");
            responseCode = 303;
        }

        OutputStream outputStream = request.beginBody(responseCode);
        PrintStream printStream = new PrintStream(outputStream);
        printStream.println("Hello World!");
    }

    public void defaultHandler(Request request) throws IOException {
        PlainTextResponse response = new PlainTextResponse();
        response.setResponseCode(404);
        response.setContent("404 Not Found");
        response.replyTo(request);
    }
}
