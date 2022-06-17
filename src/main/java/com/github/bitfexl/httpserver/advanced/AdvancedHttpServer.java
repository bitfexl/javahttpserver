package com.github.bitfexl.httpserver.advanced;

import com.github.bitfexl.httpserver.advanced.annotations.Handler;
import com.github.bitfexl.httpserver.advanced.annotations.Param;
import com.github.bitfexl.httpserver.advanced.annotations.Path;
import com.github.bitfexl.httpserver.advanced.response.Response;
import com.github.bitfexl.httpserver.simple.HttpServer;
import com.github.bitfexl.httpserver.simple.Request;
import com.sun.net.httpserver.Headers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

/**
 * A http server with advanced functionality.
 * Error messages:
 * [ERR]: The request could not be handled at all (but should have been), programming error.
 * [WRN]: The request could not be handled (probably intended) or only partially handled (no response).
 */
public class AdvancedHttpServer extends HttpServer {
    /**
     * Init a new http server with print stream set to stdout.
     *
     * @throws IOException Error creating http server.
     */
    public AdvancedHttpServer() throws IOException { }

    /**
     * Add a handler. The handler has to be marked with @Path (the path to handle),
     * and contain at least one method marked with @Handler (the http method handled by this method).
     * @param handler The handler to add.
     * @throws IllegalArgumentException Handler is not marked with @Path.
     */
    public void addHandler(Object handler) {
        Path handlerPath = handler.getClass().getAnnotation(Path.class);
        if(handlerPath == null) {
            throw new IllegalArgumentException("Handler class has to be marked with @Path (path to handle).");
        }

        setHandler(handlerPath.path(), (request) -> {
            Method handlerMethod = getHandlerMethod(handler, request);
            Object response = null;
            if(handlerMethod != null) {
                response = callHandler(handler, handlerMethod, request);
            }
            if(response instanceof Response) {
                ((Response) response).replyTo(request);
            }
        });
    }

    private Object callHandler(Object handler, Method handlerMethod, Request request) {
        String handlerMsg = request.getRawMethod() + " " + request.getPath();
        String inHandlerMsg = "in handler " + handlerMsg;

        Parameter[] methodParameters = handlerMethod.getParameters();
        Object[] params = new Object[methodParameters.length];

        for(int i=0; i<params.length; i++) {
            Parameter parameter = methodParameters[i];

            Param paramType = parameter.getAnnotation(Param.class);
            if(paramType == null) {
                printMsg("[ERR] Unknown/unannotated parameter '" + parameter.getName() + "' " +  inHandlerMsg);
                return null;
            }

            switch (paramType.value()) {
                case PATH:
                    if(!parameter.getType().equals("".getClass())) {
                        printMsg("[ERR] Parameter 'path' must by of type String " + inHandlerMsg);
                        return null;
                    }
                    params[i] = request.getPath();
                    break;
                case HEADERS:
                    HashMap<String, String> headers = getHeadersHashMap(request.getHeaders());
                    if(!parameter.getType().equals(headers.getClass())) {
                        printMsg("[ERR] Parameter 'headers' must by of type HashMap<String, String> " + inHandlerMsg);
                        return null;
                    }
                    params[i] = headers;
                    break;
                case PARAMS:
                    if(!parameter.getType().equals(request.getParameters().getClass())) {
                        printMsg("[ERR] Parameter 'params' must by of type HashMap<String, String> " + inHandlerMsg);
                        return null;
                    }
                    params[i] = request.getParameters();
                    break;
                case BODY:
                    printMsg("[ERR] Parameter 'body' currently not supported " + inHandlerMsg);
                    return null;
                    // break;
            }
        }

        Object response;
        try {
            response = handlerMethod.invoke(handler, params);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            printMsg("[ERR] Unable to call handler: " + handlerMsg + " caused by:");
            ex.printStackTrace(printStream);
            return null;
        }

        if(response == null) {
            printMsg("[WRN] Handler for: " + handlerMsg + " did not return a response.");
        }
        return response;
    }

    private Method getHandlerMethod(Object handler, Request request) {
        for(Method method : handler.getClass().getDeclaredMethods()) {
            Handler annotation = method.getDeclaredAnnotation(Handler.class);
            if(annotation != null && annotation.method() == request.getMethod()) {
                return method;
            }
        }
        printMsg("[WRN] Handler for " + request.getPath() + " does not contain a handler for method " + request.getRawMethod() + ".");
        return null;
    }

    private HashMap<String, String> getHeadersHashMap(Headers headers) {
        HashMap<String, String> headersHashMap = new HashMap<>();
        for(String header : headers.keySet()) {
            headersHashMap.put(header.toLowerCase(), headers.getFirst(header));
        }
        return headersHashMap;
    }
}
