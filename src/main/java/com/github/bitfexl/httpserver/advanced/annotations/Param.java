package com.github.bitfexl.httpserver.advanced.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    enum Type {
        PATH, PARAMS, HEADERS, BODY
    }

    /**
     * The type of this parameter.
     */
    Type value();
}
