package com.tj.sophie.job.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.ContentType;
import com.tj.sophie.job.service.IFilterService;
import com.tj.sophie.job.service.IGeneralJsonService;
import org.slf4j.Logger;

import java.text.ParseException;
import java.util.Map;

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

        this.actionService.execute(Actions.GeneralFilter, context);

        String input = context.getInput();
        boolean filtered = this.filterService.accept(input);
        if (filtered) {
            return;
        }
        try {
            ContentType contentType = (ContentType) context.getVariable("content_type");
            JsonObject jsonObject = this.generalJsonService.parse(contentType, input);
            if (jsonObject != null) {
                if (contentType == ContentType.BINGO) {
                    if (jsonObject.has("reasons") && jsonObject.has("eventId")) {
                        JsonObject reasons = jsonObject.get("reasons").getAsJsonObject();
                        jsonObject.remove("reasons");
                        String event = jsonObject.get("eventId").getAsString();

                        context.setVariable("event", event);
                        context.setVariable("reasons_json", reasons);
                        this.actionService.execute(Actions.ProcessReasons, context);
                    } else {
                        context.setError("reasons", input);
                    }
                }

                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    context.getMap("result").put(entry.getKey(), entry.getValue());
                }
            } else {
                context.setInvalid("bingo", input);
            }
        } catch (ParseException e) {
            context.setError("generalJson", input);
        }
    }
}
