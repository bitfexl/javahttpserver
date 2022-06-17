package com.github.bitfexl.httpserver.simple;

import java.util.HashMap;

public class RequestHandlerList {
    private HashMap<String, RequestHandler> handlers;

    private HashMap<String, RequestHandler> subPathHandlers;

    /**
     * The default handler to use if no handler specified.
     */
    private RequestHandler defaultHandler;

    public RequestHandlerList() {
        this.handlers = new HashMap<>();
        this.subPathHandlers = new HashMap<>();
    }

    /**
     * Register a handler.
     * @param path The path. Starting and ending with "/" (added if missing). Can have a trailing "*" for all sub paths.
     * @param handler The handler to register.
     */
    public void setHandler(String path, RequestHandler handler) {
        if(path.endsWith("*")) {
            path = fixPath(path.substring(0, path.length()-1));
            subPathHandlers.put(path, handler);
        } else {
            path = fixPath(path);
            handlers.put(path, handler);
        }
    }

    /**
     * Register a handler.
     * @param path The path. Starting and ending with "/" (added if missing).
     */
    public RequestHandler getHandler(String path) {
        path = fixPath(path);

        RequestHandler handler = handlers.get(path);
        if(handler != null) {
            return handler;
        }

        for(String possiblePath : subPathHandlers.keySet()) {
            if(path.startsWith(possiblePath)) {
                return subPathHandlers.get(possiblePath);
            }
        }

        return getDefaultHandler();
    }

    public RequestHandler getDefaultHandler() {
        return defaultHandler;
    }

    public void setDefaultHandler(RequestHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    private String fixPath(String path) {
        if(!path.startsWith("/")) {
            path = "/" + path;
        }
        if(!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }
}
