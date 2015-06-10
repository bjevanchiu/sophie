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
public class ExecutingReasonsHandler extends AbstractHandler {
    @Override
    protected void onInitialize() {
        this.setAction(Actions.ProcessReasons);
    }

    @Override
    protected void onExecute(IContext context) {
        String event = context.getVariable("event");
        if (Helper.isNullOrEmpty(event) || !event.equalsIgnoreCase("solution_executing")) {
            return;
        }
        JsonObject reasons = context.getVariable("reasons_json");
        if (!reasons.has("id")
                || !reasons.has("mem_total")
                || !reasons.has("system_free_space")
                || !reasons.has("data_free_space")
                || !reasons.has("mem_free")) {
            context.setError("reasons", reasons);
        }
        context.getMap("result").put("solution_id", reasons.get("id").getAsString());
        context.getMap("result").put("mem_total", reasons.get("mem_total").getAsString());
        context.getMap("result").put("system_free_space", reasons.get("system_free_space").getAsString());
        context.getMap("result").put("data_free_space", reasons.get("data_free_space").getAsString());
        context.getMap("result").put("mem_free", reasons.get("mem_free").getAsString());
    }
}
