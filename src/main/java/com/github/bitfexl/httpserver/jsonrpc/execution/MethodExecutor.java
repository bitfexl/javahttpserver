package com.github.bitfexl.httpserver.jsonrpc.execution;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodExecutor {
    /**
     * Execute a method.
     * @param method The method to execute.
     * @param target The object to execute the method on.
     * @param rawArgs The method parameters. Supports all primary data types and their wrappers + String.
     * @return The return value of the executed method or null if no return.
     * @throws IllegalArgumentException Arg does not match method arg.
     */
    public static Object executeMethod(Method method, Object target, String[] rawArgs) throws InvocationTargetException, IllegalAccessException {
        if(rawArgs.length != method.getParameterCount()) {
            throw new IllegalArgumentException("Args count does not match supplied args count.");
        }

        Object[] args = new Object[rawArgs.length];

        for(int i=0; i<args.length; i++) {
            args[i] = tryParseArg(rawArgs[i], method.getParameterTypes()[i]);
        }

        return method.invoke(target, args);
    }

    private static Object tryParseArg(String arg, Class<?> type) {
        try {
            return parseArg(arg, type);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Unexpected argument: '" + arg + "'", ex);
        }
    }

    private static Object parseArg(String arg, Class<?> type) {
        if(arg == null && isWrapperType(type)) {
            return null;
        } else if(type == String.class) {
            return arg;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(arg);
        } else if(type == Short.class || type == short.class) {
            return Short.parseShort(arg);
        } else if(type == Long.class || type == long.class) {
            return Long.parseLong(arg);
        } else if(type == Float.class || type == float.class) {
            return Float.parseFloat(arg);
        } else if(type == Double.class || type == double.class) {
            return Double.parseDouble(arg);
        } else if(type == Boolean.class || type == boolean.class) {
            if("true".equalsIgnoreCase(arg)) {
                return true;
            } else if("false".equalsIgnoreCase(arg)) {
                return false;
            }

            throw new IllegalArgumentException("No Boolean: '" + arg + "'");
        } else if(type == Character.class || type == char.class) {
            if(arg.length() == 1) {
                return arg.charAt(0);
            }
        }

        throw new IllegalArgumentException("Unable to parse type: '" + type.getName() + "'");
    }

    private static boolean isWrapperType(Class<?> type) {
        return type == Integer.class ||
                type == Short.class ||
                type == Long.class ||
                type == Float.class ||
                type == Double.class ||
                type == Character.class;
    }
}
