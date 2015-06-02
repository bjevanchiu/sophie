package com.tj.sophie.core;

import org.apache.commons.lang.NullArgumentException;

/**
 * Created by mbp on 6/2/15.
 */
public final class ExceptionHelper {
    public static Exception ArgumentIsNullOrEmpty(String parameterName) {
        return new NullArgumentException(String.format("Argument %s is null or empty.", parameterName));
    }
}
