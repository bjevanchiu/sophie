package com.tj.sophie.job.handler;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.Constants;
import com.tj.sophie.job.helper.Helper;
import com.tj.sophie.job.helper.JsonHelper;
import org.slf4j.Logger;

/**
 * Created by mbp on 6/10/15.
 */
@Handler
public class ActiveReasonsHandler extends AbstractHandler {
    @Inject
    private Logger logger;

    @Override
    protected void onInitialize() {
        this.setAction(Actions.ProcessReasons);
    }

    @Override
    protected void onExecute(IContext context) {
        String event = context.getVariable(Constants.Variables.EVENT_NAME);
        if (Helper.isNullOrEmpty(event) || !event.equalsIgnoreCase("active")) {
            return;
        }
        JsonObject reasons = context.getVariable(Constants.Variables.REASONS);
        if (!reasons.has("elapsed_realtime")) {
            context.setError("reasons", reasons);
        }
        JsonObject jsonObject = new JsonObject();
        JsonHelper.copyProperty("elapsed_realtime", reasons, jsonObject);

        JsonObject common = context.getVariable(Constants.Variables.COMMON_JSON);

        jsonObject = JsonHelper.join(jsonObject, common);

        context.setVariable(Constants.Variables.ACTIVE, jsonObject);
    }
}
