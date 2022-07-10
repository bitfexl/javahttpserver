package com.github.bitfexl.httpserver.jsonrpc.execution;

import com.github.bitfexl.httpserver.jsonrpc.annotations.JsonRpcMethod;
import com.github.bitfexl.httpserver.jsonrpc.http.JsonRpcErrorResponse;
import com.github.bitfexl.httpserver.jsonrpc.http.JsonRpcRequest;
import com.github.bitfexl.httpserver.jsonrpc.http.JsonRpcResponse;

import java.lang.reflect.Method;
import java.util.HashMap;

public class JsonRpcExecutor {
    protected static final String JSON_RPC_VERSION = "2.0";

    /**
     * The object to call the methods on.
     */
    protected Object rpcMethodsHolder;

    /**
     * All possible methods to call.
     */
    protected HashMap<String, Method> rpcMethods;

    public JsonRpcExecutor(Object rpcMethodsHolder) {
        this.rpcMethodsHolder = rpcMethodsHolder;
        this.rpcMethods = getRpcMethods(rpcMethodsHolder);
    }

    public JsonRpcResponse<?> call(JsonRpcRequest request) throws RpcCallException {
        Method method = rpcMethods.get(request.getMethod());
        if(method == null) {
            throw methodNotFoundException(request);
        }

        try {
            Object result = MethodExecutor.executeMethod(method, rpcMethodsHolder, request.getParams());
            return response(request, result);
        } catch (IllegalArgumentException ex) {
            throw argumentsException(request);
        } catch (Exception ex) {
            throw internalError(request);
        }
    }

    private HashMap<String, Method> getRpcMethods(Object rpcMethodsHolder) {
        Method[] methods = rpcMethodsHolder.getClass().getMethods();
        HashMap<String, Method> rpcMethods = new HashMap<>();

        for(Method method : methods) {
            if(method.getAnnotation(JsonRpcMethod.class) != null) {
                rpcMethods.put(method.getName(), method);
            }
        }

        return rpcMethods;
    }

    private RpcCallException methodNotFoundException(JsonRpcRequest request) {
        return new RpcCallException(
                new JsonRpcErrorResponse(
                        JSON_RPC_VERSION,
                        new JsonRpcErrorResponse.ErrorObject(
                                JsonRpcErrorResponse.ErrorObject.METHOD_NOT_FOUND
                        ),
                        request.getId()
                )
        );
    }

    private RpcCallException argumentsException(JsonRpcRequest request) {
        return new RpcCallException(
                new JsonRpcErrorResponse(
                        JSON_RPC_VERSION,
                        new JsonRpcErrorResponse.ErrorObject(
                                JsonRpcErrorResponse.ErrorObject.INVALID_PARAMS
                        ),
                        request.getId()
                )
        );
    }

    private RpcCallException internalError(JsonRpcRequest request) {
        return new RpcCallException(
                new JsonRpcErrorResponse(
                        JSON_RPC_VERSION,
                        new JsonRpcErrorResponse.ErrorObject(
                                JsonRpcErrorResponse.ErrorObject.INTERNAL_ERROR
                        ),
                        request.getId()
                )
        );
    }

    private JsonRpcResponse<?> response(JsonRpcRequest request, Object result) {
        return new JsonRpcResponse(JSON_RPC_VERSION, result, request.getId());
    }
}
