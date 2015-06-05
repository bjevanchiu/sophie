package com.tj.sophie.job.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.Action;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mbp on 6/5/15.
 */
@Handler
public class MainJobHandler extends AbstractHandler {

    private Gson gson = new Gson();
    private Pattern pattern = Pattern.compile("[^\\{](?<json>\\{.*\\})");

    @Inject
    private IActionService actionService;

    @Override
    protected void onExecute(IContext context) {
        String input = context.getInput();
        Matcher matcher = pattern.matcher(input);
        String jsonString = null;
        while (matcher.find()) {
            jsonString = matcher.group("json");
        }
        if (jsonString != null && !jsonString.trim().isEmpty()) {
            JsonObject json = this.gson.fromJson(jsonString, JsonObject.class);
            context.setVariable("json", json);
        }
        this.actionService.execute(Action.create("process", "process"), context);
    }

    @Override
    protected void onInitialize() {
        this.setAction(Action.create("mainjob", "mainjob"));
    }
}
