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
import com.tj.sophie.job.helper.JsonHelper;
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
public class BingoLogHandler extends AbstractHandler {
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
        this.setAction(Actions.BingoLog);
    }

    @Override
    protected void onExecute(IContext context) {

        String input = context.getInput();
        boolean filtered = this.filterService.accept(input);
        if (filtered) {
            Map<String, Object> filters = context.getMap(Constants.Variables.FILTERS);
            filters.put(UUID.randomUUID().toString(), input);
            context.setVariable(Constants.Variables.FILTER_FLAG, true);
            return;
        }
        try {
            ContentType contentType = context.getVariable(Constants.Variables.CONTENT_TYPE);
            JsonObject jsonObject = this.generalJsonService.parse(contentType, input);
            if (jsonObject != null) {
                if (this.filterService.acceptEvent("app_process_error", jsonObject)
                        || this.filterService.acceptEvent("checker", jsonObject)
                        || this.filterService.acceptEvent("devs", jsonObject)
                        || this.filterService.acceptEvent("dns_error", jsonObject)
                        || this.filterService.acceptEvent("network_error", jsonObject)
                        || this.filterService.acceptEvent("process", jsonObject)
                        || this.filterService.acceptEvent("props", jsonObject)
                        || this.filterService.acceptEvent("ps", jsonObject)
                        || this.filterService.acceptEvent("release", jsonObject)) {
                    context.setVariable(Constants.Variables.FILTER_FLAG, true);
                    return;
                }
                context.setVariable(Constants.Variables.ORIGIN_JSON, jsonObject);
                context.setVariable(Constants.Variables.ORIGIN_JSON, jsonObject);
                JsonObject commonJson = JsonHelper.bingoCommon(jsonObject);
                context.setVariable(Constants.Variables.COMMON_JSON, commonJson);
                JsonObject reasons = jsonObject.get("reasons").getAsJsonObject();
                context.setVariable(Constants.Variables.REASONS, reasons);
                String event = commonJson.get("eventId").getAsString();
                if (commonJson == null || reasons == null || event == null) {
                    context.setError("json", input);
                    return;
                }
                context.setVariable(Constants.Variables.EVENT_NAME, event);
                this.actionService.execute(Actions.ProcessReasons, context);
            } else {
                context.setInvalid("bingo", input);
            }
        } catch (ParseException e) {
            context.setError("generalJson", input);
        }
    }
}
