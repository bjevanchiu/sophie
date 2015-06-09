package com.tj.sophie.job.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.service.Actions;

import java.util.Map;

/**
 * Created by mbp on 6/5/15.
 */
@Handler
public class GeneralJsonHandler extends AbstractHandler {
    @Override
    protected void onInitialize() {
        this.setAction(Actions.GeneralJson);
    }

    @Override
    protected void onExecute(IContext context) {
        JsonObject json = context.getVariable("json");
        if (json == null) {
            return;
        }
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            context.setResult(entry.getKey(), entry.getValue());
        }
    }
}
