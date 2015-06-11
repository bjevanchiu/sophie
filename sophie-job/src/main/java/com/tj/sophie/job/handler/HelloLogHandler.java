package com.tj.sophie.job.handler;

import com.google.gson.Gson;
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
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by mbp on 6/8/15.
 */
@Handler
public class HelloLogHandler extends AbstractHandler {
    private Gson gson = new Gson();
    private Pattern pattern = Pattern.compile("^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) (?<ssid>\\d+) *(?<level>\\w)+ *\\[(?<class>.*?)\\] *\\(.*?\\) *(?<json>\\{.*\\})?");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SS");

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
        this.actionService.execute(Actions.GeneralFilter, context);

        String input = context.getInput();
        boolean filtered = this.filterService.accept(input);
        if (filtered) {
            return;
        }
        try {
            JsonObject jsonObject = this.generalJsonService.parse((ContentType) context.getVariable("content_type"), input);
            if (jsonObject != null) {




                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    context.getMap("result").put(entry.getKey(), entry.getValue());
                }
            } else {
                context.setInvalid("hello", input);
            }
        } catch (ParseException e) {
            context.setError("generalJson", input);
        }
    }
}
