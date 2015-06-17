package com.tj.sophie.job.handler;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IActionService;
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
public class ExecutedReasonsHandler extends AbstractHandler {

    @Inject
    private IActionService actionService;

    @Override
    protected void onInitialize() {
        this.setAction(Actions.ProcessReasons);
    }

    @Override
    protected void onExecute(IContext context) {
        String event = context.getVariable(Constants.Variables.EVENT_NAME);
        if (Helper.isNullOrEmpty(event) || !event.equalsIgnoreCase("solution_executed")) {
            return;
        }
        JsonObject reasons = context.getVariable(Constants.Variables.REASONS);
        if (!reasons.has("id")
                || !reasons.has("exit_id")
                || !reasons.has("core_log")
                || !reasons.has("status")
                || !reasons.has("times")
                || !reasons.has("log")
                || !reasons.has("message")) {
            context.setError("reasons", reasons);
        }

        JsonObject jsonObject = new JsonObject();

        String solution_id = reasons.get("id").getAsString();
        jsonObject.addProperty("solution_id", solution_id);
        JsonHelper.copyProperty("exit_id", reasons, jsonObject);
        JsonHelper.copyProperty("status", reasons, jsonObject);
        JsonHelper.copyProperty("times", reasons, jsonObject);
        JsonHelper.copyProperty("message", reasons, jsonObject);

        JsonObject common = context.getVariable(Constants.Variables.COMMON_JSON);

        jsonObject = JsonHelper.join(jsonObject, common);

        context.setVariable(Constants.Variables.EXECUTED, jsonObject);

        this.actionService.execute(Actions.ProcessKnownRootAddress, context);

    }


}
