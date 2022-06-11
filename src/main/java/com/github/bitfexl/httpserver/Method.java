package com.github.bitfexl.httpserver;

// https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods

public enum Method {
    GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH, UNKNOWN;

    public static Method fromString(String rawMethod) {
        for(Method method : new Method[]{ GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH }) {
            if(rawMethod.equalsIgnoreCase(method.toString())) {
                return method;
            }
        }
        return UNKNOWN;
    }
}
