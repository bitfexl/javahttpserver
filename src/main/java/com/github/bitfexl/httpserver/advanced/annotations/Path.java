package com.github.bitfexl.httpserver.advanced.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Path {
    /**
     * The path this class handles. Can have a trailing "*" for all sub paths.
     */
    String path();
}
