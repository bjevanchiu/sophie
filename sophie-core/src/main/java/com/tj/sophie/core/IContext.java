package com.tj.sophie.core;

import java.util.Map;
import java.util.Set;

/**
 * Created by mbp on 6/3/15.
 */
public interface IContext {

    String getInput();

    <T> T getVariable(String key);

    <T> void setVariable(String key, T value);

    Set<Map.Entry<String, Object>> getVariableEntries();

    <T> T  getResult(String key);

    <T> void setResult(String key, T value);

    Set<Map.Entry<String, Object>> getResultEntries();
}
