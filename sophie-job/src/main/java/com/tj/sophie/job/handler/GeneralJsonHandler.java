package com.tj.sophie.job.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.Action;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mbp on 6/5/15.
 */
@Handler
public class GeneralJsonHandler extends AbstractHandler {

    @Override
    protected void onExecute(IContext context) {
        JsonObject json = context.getVariable("json");
        if (json == null) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String key = entry.getKey();
            JsonElement instance = entry.getValue();
            if (instance.isJsonPrimitive()) {
                JsonPrimitive primitive = instance.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    map.put(key, Integer.toString(primitive.getAsInt()));
                } else if (primitive.isBoolean()) {
                    map.put(key, Boolean.toString(primitive.getAsBoolean()));
                } else if (primitive.isString()) {
                    map.put(key, primitive.getAsString());
                }
            }
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            context.setResult(entry.getKey(), entry.getValue());
        }
    }

    @Override
    protected void onInitialize() {
        this.setAction(Action.create("process", "process"));
    }
}
