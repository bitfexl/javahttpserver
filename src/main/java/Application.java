import com.github.bitfexl.httpserver.HttpServer;
import com.github.bitfexl.httpserver.Request;
import com.github.bitfexl.httpserver.RequestHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

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
        HttpServer server = new HttpServer().start(65500);
        server.setHandler("/api/*", this);
        // server.stop();
    }

    @Override
    public void handleRequest(Request request) throws IOException {
        System.out.println("Handling " + request.getPath());
        OutputStream outputStream = request.beginBody(200);
        PrintStream printStream = new PrintStream(outputStream);
        printStream.println("Hello World!");
    }
}