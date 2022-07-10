package com.github.bitfexl.httpserver.jsonrpc;

import com.github.bitfexl.httpserver.jsonrpc.execution.JsonRpcExecutor;
import com.github.bitfexl.httpserver.jsonrpc.execution.RpcCallException;
import com.github.bitfexl.httpserver.jsonrpc.http.JsonRpcRequest;
import com.github.bitfexl.httpserver.simple.HttpServer;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * implementing: https://www.jsonrpc.org/specification
 * Does not fully implement the specification.
 */
public class JsonRpcServer extends HttpServer {

    private Gson gson;

    /**
     * Init a new http server with print stream set to stdout.
     *
     * @throws IOException Error creating http server.
     */
    public JsonRpcServer() throws IOException {
        this.gson = new Gson();
    }

    @Override
    public JsonRpcServer start(int port) throws IOException {
        super.start(port);
        return this;
    }

    @Override
    public JsonRpcServer start(int port, int backlog) throws IOException {
        super.start(port, backlog);
        return this;
    }

    /**
     * Add a json rpc handler which can be called using json rpc requests.
     * @param path The path of the handler.
     * @param handler A handler containing public methods tagged with @JsonRpcMethod.
     */
    public void setJsonRpcHandler(String path, Object handler) {
        JsonRpcExecutor executor = new JsonRpcExecutor(handler);

        setHandler(path, (request) -> {
            String body = new String(request.getRequestBody().readAllBytes());

            JsonRpcRequest rpcRequest;
            try {
                rpcRequest = gson.fromJson(body, JsonRpcRequest.class);
            } catch (JsonParseException ex) {
                request.beginBody(400, 0);
                return;
            }
            if(rpcRequest == null) {
                request.beginBody(400, 0);
                return;
            }

            String responseJson;
            try {
                responseJson = gson.toJson(executor.call(rpcRequest));
            } catch (RpcCallException ex) {
                responseJson = gson.toJson(ex.getJsonRpcError());
            }

            request.setHeader("Content-Type", "application/json");

            byte[] bytes = responseJson.getBytes(StandardCharsets.UTF_8);
            OutputStream outputStream = request.beginBody(200, bytes.length);
            outputStream.write(bytes);
        });
    }
}
