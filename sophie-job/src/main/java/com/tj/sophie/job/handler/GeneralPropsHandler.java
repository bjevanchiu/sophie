package com.tj.sophie.job.handler;

import com.google.gson.JsonObject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.job.Actions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mbp on 6/9/15.
 */
public class GeneralPropsHandler extends AbstractHandler {
    @Override
    protected void onInitialize() {
        this.setAction(Actions.GeneralProps);
    }

    @Override
    protected void onExecute(IContext context) {
        JsonObject jsonObject = context.getVariable("json");
        if (jsonObject == null) {
            return;
        }
        String seedVersion = null;
        String event = null;
        if (jsonObject.has("eventId")) {
            event = jsonObject.get("eventId").getAsString();
        }

        if (event != null && event.equalsIgnoreCase("props")) {
            Map<String, JsonObject> props = new HashMap<>();
            if (jsonObject.has("reasons")) {
                JsonObject reasons = jsonObject.getAsJsonObject("reasons");
                if (reasons.entrySet().isEmpty()) {
                    context.setError("props", context.getInput());
                } else {
                    context.setVariable("props", reasons);
                }
            } else {
                context.setError("props", context.getInput());
            }
        }
    }
}
