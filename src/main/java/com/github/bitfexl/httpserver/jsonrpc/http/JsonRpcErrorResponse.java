package com.github.bitfexl.httpserver.jsonrpc.http;

public class JsonRpcErrorResponse {
    public static class ErrorObject {
        public static final int PARSE_ERROR = -32700;
        public static final int INVALID_REQUEST = -32600;
        public static final int METHOD_NOT_FOUND = -32601;
        public static final int INVALID_PARAMS = -32602;
        public static final int INTERNAL_ERROR = -32603;
        public static final int SERVER_ERROR = -32000;

        /**
         * Error code.
         */
        private int code;

        /**
         * Error message.
         */
        private String message;

        public ErrorObject(int code) {
            this.code = code;
            this.message = getMessage(code);
        }

        public ErrorObject(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        private String getMessage(int code) {
            return switch (code) {
                case PARSE_ERROR -> "Parse error";
                case INVALID_REQUEST -> "Invalid Request";
                case METHOD_NOT_FOUND -> "Method not found";
                case INVALID_PARAMS -> "Invalid params";
                case INTERNAL_ERROR -> "Internal error";
                case SERVER_ERROR -> "Server error";
                default -> "";
            };
        }
    }

    /**
     * version
     */
    private String jsonrpc;

    /**
     * Method result.
     */
    private ErrorObject error;

    /**
     * The id, only Integer supported.
     */
    private Integer id;

    public JsonRpcErrorResponse(String jsonrpc, ErrorObject error, Integer id) {
        this.jsonrpc = jsonrpc;
        this.error = error;
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public ErrorObject getError() {
        return error;
    }

    public Integer getId() {
        return id;
    }
}
