package com.tj.sophie.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mbp on 6/2/15.
 */
public final class Context {

    private Map<String, Object> map = new HashMap<>();

    public synchronized <T> void set(String key, T value) {
        this.map.put(key, value);
    }

    public synchronized <T> T get(String key) {
        if (!this.map.containsKey(key)) {
            return null;
        }
        return (T) this.map.get(key);
    }

    public Set<Map.Entry<String, Object>> getEntries() {
        return this.map.entrySet();
    }
}
