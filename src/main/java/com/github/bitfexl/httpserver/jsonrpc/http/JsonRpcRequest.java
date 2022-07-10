package com.github.bitfexl.httpserver.jsonrpc.http;

/**
 * Json rpc request for parsing json.
 * Does not fully implement the specification.
 */
public class JsonRpcRequest {
    /**
     * version
     */
    private String jsonrpc;

    /**
     * Method to call.
     */
    private String method;

    /**
     * Parameters, only array supported.
     */
    private String[] params;

    /**
     * The id, only Integer supported.
     */
    private Integer id;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public String[] getParams() {
        return params;
    }

    public Integer getId() {
        return id;
    }
}
