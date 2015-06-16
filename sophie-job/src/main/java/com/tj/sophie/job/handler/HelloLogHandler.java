package com.tj.sophie.job.handler;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.Constants;
import com.tj.sophie.job.ContentType;
import com.tj.sophie.job.service.IFilterService;
import com.tj.sophie.job.service.IGeneralJsonService;
import org.slf4j.Logger;

import java.text.ParseException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by mbp on 6/8/15.
 */
@Handler
public class HelloLogHandler extends AbstractHandler {
    @Inject
    private Logger logger;

    @Inject
    private IActionService actionService;
    @Inject
    private IFilterService filterService;

    @Inject
    private IGeneralJsonService generalJsonService;

    @Override
    protected void onInitialize() {
        this.setAction(Actions.HelloLog);
    }

    @Override
    protected void onExecute(IContext context) {
        String input = context.getInput();
        boolean filtered = this.filterService.accept(input);
        if (filtered) {
            Map<String, Object> filters = context.getMap(Constants.Variables.FILTERS);
            filters.put(UUID.randomUUID().toString(), input);
            return;
        }
        try {
            JsonObject jsonObject = this.generalJsonService.parse((ContentType) context.getVariable(Constants.Variables.CONTENT_TYPE), input);
            if (jsonObject != null) {
                context.setVariable(Constants.Variables.ORIGIN_JSON, jsonObject);
                this.actionService.execute(Actions.GeneralDeliver, context);
            }
        } catch (ParseException e) {
            context.setError("generalJson", input);
        }
    }
}
