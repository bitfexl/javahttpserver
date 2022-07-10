package com.github.bitfexl.httpserver.jsonrpc.http;

/**
 * Json rpc response for json serialization.
 * Does not fully implement the specification.
 */
public class JsonRpcResponse<T> {
    /**
     * version
     */
    private String jsonrpc;

    /**
     * Method result.
     */
    private T result;

    /**
     * The id, only Integer supported.
     */
    private Integer id;

    public JsonRpcResponse(String jsonrpc, T result, Integer id) {
        this.jsonrpc = jsonrpc;
        this.result = result;
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public T getResult() {
        return result;
    }

    public Integer getId() {
        return id;
    }
}
