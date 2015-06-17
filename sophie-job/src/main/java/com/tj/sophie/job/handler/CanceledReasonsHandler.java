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
public class CanceledReasonsHandler extends AbstractHandler {
    @Override
    protected void onInitialize() {
        this.setAction(Actions.ProcessReasons);
    }

    @Override
    protected void onExecute(IContext context) {
        String event = context.getVariable(Constants.Variables.EVENT_NAME);
        if (Helper.isNullOrEmpty(event) || !event.equalsIgnoreCase("solutionCanceled")) {
            return;
        }
        JsonObject reasons = context.getVariable(Constants.Variables.REASONS);
        JsonObject jsonObject = new JsonObject();
        if (!reasons.has("id")) {
            context.setError("reasons", reasons);
        }
        String solution_id = reasons.get("id").getAsString();
        jsonObject.addProperty("solution_id", solution_id);
        JsonObject common = context.getVariable(Constants.Variables.COMMON_JSON);
        jsonObject = JsonHelper.join(jsonObject, common);
        context.setVariable(Constants.Variables.CANCELED, jsonObject);
    }
}
