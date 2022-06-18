package com.github.bitfexl.httpserver.example.endpoints;

import com.github.bitfexl.httpserver.Method;
import com.github.bitfexl.httpserver.advanced.annotations.Handler;
import com.github.bitfexl.httpserver.advanced.annotations.Param;
import com.github.bitfexl.httpserver.advanced.annotations.Path;
import com.github.bitfexl.httpserver.advanced.response.PlainTextResponse;

import java.util.HashMap;

@Path(path = "/log/*")
public class LoggingHandler {

    @Handler(method = Method.GET)
    public Object log(
        @Param(Param.Type.PARAMS) HashMap<String, String> params,
        @Param(Param.Type.HEADERS) HashMap<String, String> headers,
        @Param(Param.Type.PATH) String path
    ) {
        System.out.println("Logging at: " + path);
        System.out.println("Message from: " + headers.get("user-agent"));
        for(String param : params.keySet()) {
            System.out.println(param + "=" + params.get(param));
        }
        return PlainTextResponse.of("Logged!");
    }
}
