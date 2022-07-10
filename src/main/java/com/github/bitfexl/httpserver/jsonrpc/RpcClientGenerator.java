package com.github.bitfexl.httpserver.jsonrpc;

import com.github.bitfexl.httpserver.jsonrpc.execution.JsonRpcExecutor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RpcClientGenerator {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    /**
     * Generate javascript client "library" for a json rpc handler.
     * @param url The complete url for the webserver.
     * @param handler The handler with @JsonRpcMethod handler methods.
     * @return The generated js code/functions.
     */
    public String jsForHandler(String url, Object handler) {
        Map<String, Method> rpcMethods = getJsonRpcMethods(handler);

        StringBuilder jsCode = new StringBuilder("""
                const jsonRpc = {
                    "URL": "{URL}",
                """.replace("{URL}", url));

        for(String method : rpcMethods.keySet()) {
            jsCode.append(jsForMethod(rpcMethods.get(method)).indent(4));
        }

        return jsCode.append("};").toString();
    }

    private String jsForMethod(Method method) {
        String params = String.join(", ", ALPHABET.substring(0, method.getParameterCount()).split(""));
        return """
                ["{METHOD_NAME}"]: async function({PARAMS}) {
                    return await (
                        await fetch(this.URL, {
                            method: "POST",
                            body: JSON.stringify({
                                jsonrpc: "2.0",
                                method: "{METHOD_NAME}",
                                params: [{PARAMS}],
                                id: 0,
                            }),
                        })
                    ).json();
                },
                """.replace("{METHOD_NAME}", method.getName()).replace("{PARAMS}", params);
    }

    private Map<String, Method> getJsonRpcMethods(Object handler) {
        class OpenJsonRpcExecutor extends JsonRpcExecutor {
            public OpenJsonRpcExecutor(Object rpcMethodsHolder) {
                super(rpcMethodsHolder);
            }

            public HashMap<String, Method> getRpcMethods() {
                return this.rpcMethods;
            }
        }

        return new OpenJsonRpcExecutor(handler).getRpcMethods();
    }
}
