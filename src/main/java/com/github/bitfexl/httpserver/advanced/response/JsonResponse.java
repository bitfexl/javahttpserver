package com.github.bitfexl.httpserver.advanced.response;

public class JsonResponse extends PlainTextResponse {
    /**
     * Create a new json response.
     * @param code The http status code.
     * @param content The response content.
     * @return The new response.
     */
    public static JsonResponse of(int code, String content) {
        JsonResponse response = new JsonResponse();
        response.setResponseCode(code);
        response.setContent(content);
        return response;
    }

    /**
     * Create a new json response.
     * @param content The response content.
     * @return The new response.
     */
    public static JsonResponse of(String content) {
        return of(200, content);
    }

    public JsonResponse() {
        setHeader("Content-Type", "application/json");
    }
}
