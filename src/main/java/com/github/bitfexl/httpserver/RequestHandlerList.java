package com.github.bitfexl.httpserver;

import java.util.HashMap;

public class RequestHandlerList {
    private HashMap<String, RequestHandler> handlers;

    /**
     * The default handler to use if no handler specified.
     */
    private RequestHandler defaultHandler;

    public RequestHandlerList() {
        this.handlers = new HashMap<>();
    }

    /**
     * Register a handler.
     * @param path The path. Starting and ending with "/" (added if missing). Can have a trailing "*" for all sub paths.
     * @param handler The handler to register.
     */
    public void setHandler(String path, RequestHandler handler) {
        path = fixPath(path);
        if(path.endsWith("*/")) {
            path = path.substring(0, path.length()-1);
        }
        handlers.put(path, handler);
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

        for(String possiblePath : handlers.keySet()) {
            if(possiblePath.endsWith("*")) {
                String pathStart = possiblePath.substring(0, possiblePath.length()-1);
                if(path.startsWith(pathStart)) {
                    return handlers.get(possiblePath);
                }
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
