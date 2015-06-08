package com.tj.sophie.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mbp on 6/2/15.
 */
public final class Context implements IContext {

    private Map<String, Object> variableMap = new HashMap<>();
    private Map<String, Object> resultMap = new HashMap<>();

    private String input = null;

    public Context(String input) {
        this.input = input;
        if (input == null || input.trim().isEmpty()) {
            this.input = input.trim();
        }
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
    public Set<Map.Entry<String, Object>> getVariableEntries() {
        return this.variableMap.entrySet();
    }

    @Override
    public <T> T getResult(String key) {
        if (this.resultMap.containsKey(key)) {
            return (T) resultMap.get(key);
        }
        return null;
    }

    @Override
    public <T> void setResult(String key, T value) {
        this.resultMap.put(key, value);
    }

    @Override
    public Set<Map.Entry<String, Object>> getResultEntries() {
        return this.resultMap.entrySet();
    }
}
