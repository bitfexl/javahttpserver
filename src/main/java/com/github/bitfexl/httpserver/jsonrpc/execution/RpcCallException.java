package com.github.bitfexl.httpserver.jsonrpc.execution;

import com.github.bitfexl.httpserver.jsonrpc.http.JsonRpcErrorResponse;

public class RpcCallException extends Exception {
    /**
     * The corresponding json rpc error object.
     */
    private JsonRpcErrorResponse jsonRpcError;

    public RpcCallException(JsonRpcErrorResponse jsonRpcError) {
        this.jsonRpcError = jsonRpcError;
    }

    public RpcCallException(String message, JsonRpcErrorResponse jsonRpcError) {
        super(message);
        this.jsonRpcError = jsonRpcError;
    }

    public RpcCallException(String message, Throwable cause, JsonRpcErrorResponse jsonRpcError) {
        super(message, cause);
        this.jsonRpcError = jsonRpcError;
    }

    public JsonRpcErrorResponse getJsonRpcError() {
        return jsonRpcError;
    }
}
