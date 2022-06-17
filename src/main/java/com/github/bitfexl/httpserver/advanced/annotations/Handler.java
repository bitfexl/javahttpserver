package com.github.bitfexl.httpserver.advanced.annotations;

import com.github.bitfexl.httpserver.Method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Handler {
    /**
     * The http method this handler handles.
     */
    Method method();
}
