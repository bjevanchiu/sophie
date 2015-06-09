package com.tj.sophie.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mbp on 6/2/15.
 */
public final class Context implements IContext {

    private Map<String, Object> variableMap = new HashMap<>();
    private Map<String, Object> resultMap = new HashMap<>();
    private Map<String, Object> errorMap = new HashMap<>();
    private Map<String, Object> invalidMap = new HashMap<>();

    private String input = null;

    public Context(String input) {
        this.input = input;
    }


    @Override
    public String getInput() {
        return this.input;
    }

    @Override
    public <T> T getVariable(String key) {
        if (this.variableMap.containsKey(key)) {
            return (T) variableMap.get(key);
        }
        return null;
    }

    @Override
    public <T> void setVariable(String key, T value) {
        this.variableMap.put(key, value);
    }

    @Override
    public Map<String, Object> getVariableMap() {
        return this.variableMap;
    }

    @Override
    public <T> T getResult(String key) {
        if (this.resultMap.containsKey(key)) {
            return (T) this.resultMap.get(key);
        }
        return null;
    }

    @Override
    public <T> void setResult(String key, T value) {
        this.resultMap.put(key, value);
    }

    @Override
    public Map<String, Object> getResultMap() {
        return this.resultMap;
    }

    @Override
    public <T> T getError(String key) {
        if (this.errorMap.containsKey(key)) {
            return (T) this.errorMap.get(key);
        }
        return null;
    }

    @Override
    public <T> void setError(String key, T value) {
        this.errorMap.put(key, value);
    }

    @Override
    public Map<String, Object> getErrorMap() {
        return this.errorMap;
    }

    @Override
    public <T> T getInvalid(String key) {
        if (this.invalidMap.containsKey(key)) {
            return (T) this.invalidMap.get(key);
        }
        return null;
    }

    @Override
    public <T> void setInvalid(String key, T value) {
        this.invalidMap.put(key, value);
    }

    @Override
    public Map<String, Object> getInvalidMap() {
        return this.invalidMap;
    }
}
