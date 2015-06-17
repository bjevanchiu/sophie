package com.tj.sophie.job.handler;

import com.google.gson.JsonObject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.Constants;
import com.tj.sophie.job.helper.Helper;
import com.tj.sophie.job.helper.JsonHelper;

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
        String event = context.getVariable(Constants.Variables.EVENT_NAME);
        if (Helper.isNullOrEmpty(event) || !event.equalsIgnoreCase("solution_executing")) {
            return;
        }
        JsonObject reasons = context.getVariable(Constants.Variables.REASONS);
        if (!reasons.has("id")
                || !reasons.has("mem_total")
                || !reasons.has("system_free_space")
                || !reasons.has("data_free_space")
                || !reasons.has("mem_free")) {
            context.setError("reasons", reasons);
        }

        JsonObject jsonObject = new JsonObject();

        String solution_id = reasons.get("id").getAsString();
        jsonObject.addProperty("solution_id", solution_id);
        JsonHelper.copyProperty("mem_total", reasons, jsonObject);
        JsonHelper.copyProperty("system_free_space", reasons, jsonObject);
        JsonHelper.copyProperty("data_free_space", reasons, jsonObject);
        JsonHelper.copyProperty("mem_free", reasons, jsonObject);

        JsonObject common = context.getVariable(Constants.Variables.COMMON_JSON);

        jsonObject = JsonHelper.join(jsonObject, common);

        context.setVariable(Constants.Variables.EXECUTING, jsonObject);
    }
}
