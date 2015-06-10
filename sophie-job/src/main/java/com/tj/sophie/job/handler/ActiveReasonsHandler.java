package com.tj.sophie.job.handler;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.helper.Helper;
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
        String event = context.getVariable("event");
        if (Helper.isNullOrEmpty(event) || !event.equalsIgnoreCase("active")) {
            return;
        }
        JsonObject reasons = context.getVariable("reasons_json");
        if (!reasons.has("elapsed_realtime")) {
            context.setError("reasons", reasons);
        }
        this.logger.info(String.format("process %s reasons, reasons %s", event, reasons.toString()));
        context.getMap("result").put("elapsed_realtime", reasons.get("elapsed_realtime").getAsString());
    }
}
