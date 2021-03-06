package com.tj.sophie.core;

import java.util.Map;

/**
 * Created by mbp on 6/3/15.
 */
public interface IContext {

    String getInput();

    <T> T getVariable(String key);

    <T> void setVariable(String key, T value);

    Map<String, Object> getVariableMap();

    <T> T getError(String key);

    <T> void setError(String key, T value);

    Map<String, Object> getErrorMap();

    <T> T getInvalid(String key);

    <T> void setInvalid(String key, T value);

    Map<String, Object> getInvalidMap();

    Map<String, Object> getMap(String key);

    Map<String, Map<String, Object>> getMaps();

}
