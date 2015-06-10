package com.tj.sophie.job.handler;

import com.google.gson.JsonObject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.helper.Helper;

/**
 * Created by mbp on 6/10/15.
 */
@Handler
public class CanceledReasonsHandler extends AbstractHandler {
    @Override
    protected void onInitialize() {
        this.setAction(Actions.ProcessReasons);
    }

    @Override
    protected void onExecute(IContext context) {
        String event = context.getVariable("event");
        if (Helper.isNullOrEmpty(event) || !event.equalsIgnoreCase("solutionCanceled")) {
            return;
        }
        JsonObject reasons = context.getVariable("reasons_json");
        if (!reasons.has("id")) {
            context.setError("reasons", reasons);
        }
        context.getMap("result").put("solution_id", reasons.get("id").getAsString());
    }
}
