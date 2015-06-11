package com.tj.sophie.job.handler;

import com.google.gson.JsonObject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.helper.Helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mbp on 6/10/15.
 */
@Handler
public class ExecutedReasonsHandler extends AbstractHandler {

    private Pattern supportPattern = Pattern.compile("\\\\n\\d+:info:\\\\t读写器在此机器上是否支持 *(?<support>\\d+) *\\.");

    @Override
    protected void onInitialize() {
        this.setAction(Actions.ProcessReasons);
    }

    @Override
    protected void onExecute(IContext context) {
        String event = context.getVariable("event");
        if (Helper.isNullOrEmpty(event) || !event.equalsIgnoreCase("solution_executed")) {
            return;
        }
        JsonObject reasons = context.getVariable("reasons_json");
        if (!reasons.has("id")
                || !reasons.has("exit_id")
                || !reasons.has("core_log")
                || !reasons.has("status")
                || !reasons.has("times")
                || !reasons.has("log")
                || !reasons.has("message")) {
            context.setError("reasons", reasons);
        }
        context.getMap("result").put("solution_id", reasons.get("id").getAsString());
        context.getMap("result").put("exit_id", reasons.get("exit_id").getAsString());
        context.getMap("result").put("status", reasons.get("status").getAsString());
        context.getMap("result").put("times", reasons.get("times").getAsString());
        context.getMap("result").put("message", reasons.get("message").getAsString());
        String log = reasons.get("log").getAsString();
        String status = reasons.get("status").getAsString();
        if (status.equalsIgnoreCase("fail")) {
            Matcher matcher = supportPattern.matcher(log);
            while (matcher.find()) {
                String support = matcher.group("support");
                if (Helper.equalsIgnoreCase(support, "0")) {
                    context.getMap("result").put("solution_support", "false");
                    return;
                }
            }
        }
    }
}
