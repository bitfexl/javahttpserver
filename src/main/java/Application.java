import com.github.bitfexl.httpserver.Method;
import com.github.bitfexl.httpserver.advanced.AdvancedHttpServer;
import com.github.bitfexl.httpserver.advanced.annotations.Handler;
import com.github.bitfexl.httpserver.advanced.annotations.Param;
import com.github.bitfexl.httpserver.advanced.annotations.Path;
import com.github.bitfexl.httpserver.advanced.response.HTMLResponse;
import com.github.bitfexl.httpserver.advanced.response.PlainTextResponse;
import com.github.bitfexl.httpserver.advanced.response.Response;
import com.github.bitfexl.httpserver.simple.HttpServer;
import com.github.bitfexl.httpserver.simple.Request;
import com.github.bitfexl.httpserver.simple.RequestHandler;

import javax.imageio.plugins.tiff.GeoTIFFTagSet;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;

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
        server.setDefaultHandler(this::defaultHandler);
        server.setHandler("api/*", this);
        server.addHandler(this);
        server.start(65500);
        // server.stop();
    }

    /// advanced version ///
    @Handler(method = Method.GET)
    public Response helloWorld(
            @Param(Param.Type.PARAMS) HashMap<String, String> params,
            @Param(Param.Type.HEADERS) HashMap<String, String> headers) {

        System.out.println("Request from: " + headers.get("user-agent"));

        for(String param : params.keySet()) {
            System.out.println(param + "=" + params.get(param));
        }

        PlainTextResponse response = new HTMLResponse();
        response.setContent("<h1>Hello World!!!!!</h1>This is a test.");
        return response;
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
