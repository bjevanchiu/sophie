package com.tj.sophie.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mbp on 6/2/15.
 */
public final class Context implements IContext {

    private Map<String, Object> variableMap = new HashMap<>();
    private Map<String, Object> errorMap = new HashMap<>();
    private Map<String, Object> invalidMap = new HashMap<>();

    private Map<String, Map<String, Object>> map = new HashMap<>();

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
        return this.get(key, this.variableMap);
    }

    @Override
    public <T> void setVariable(String key, T value) {
        this.set(key, value, this.variableMap);
    }

    @Override
    public Map<String, Object> getVariableMap() {
        return this.variableMap;
    }

    @Override
    public <T> T getError(String key) {
        return this.get(key, this.errorMap);
    }

    @Override
    public <T> void setError(String key, T value) {
        this.set(key, value, this.errorMap);
    }

    @Override
    public Map<String, Object> getErrorMap() {
        return this.errorMap;
    }

    @Override
    public <T> T getInvalid(String key) {
        return this.get(key, this.invalidMap);
    }

    @Override
    public <T> void setInvalid(String key, T value) {
            key = key.trim();
            this.invalidMap.put(key, value);
    }

    @Override
    public Map<String, Object> getInvalidMap() {
        return this.invalidMap;
    }

    @Override
    public Map<String, Object> getMap(String key) {
        key = key.trim();
        if (!this.map.containsKey(key)) {
            this.map.put(key, new HashMap<String, Object>());
        }
        return this.map.get(key);
    }

    @Override
    public Map<String, Map<String, Object>> getMaps() {
        return this.map;
    }


    private <T> T get(String key, Map<String, Object> map) {
        key = key.trim();
        if (map.containsKey(key)) {
            return (T) map.get(key);
        }
        return null;
    }

    private void set(String key, Object value, Map<String, Object> map) {
        key = key.trim();
        map.put(key, value);
    }
}
