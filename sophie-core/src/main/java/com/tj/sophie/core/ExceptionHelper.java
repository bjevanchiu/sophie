package com.tj.sophie.core;

/**
 * Created by mbp on 6/2/15.
 */
public final class ExceptionHelper {
    public static IllegalArgumentException ArgumentIsNullOrEmpty(String parameterName) {
        return new IllegalArgumentException(String.format("Argument %s is null or empty.", parameterName));
    }
}
