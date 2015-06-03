package com.tj.sophie.core;

/**
 * Created by mbp on 6/3/15.
 */
public interface IContext {
    <T> T get(String key);

    <T> void set(String key, T value);
}
