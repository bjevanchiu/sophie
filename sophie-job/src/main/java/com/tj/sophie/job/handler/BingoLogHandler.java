package com.tj.sophie.job.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.Constants;
import org.slf4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mbp on 6/8/15.
 */
@Handler
public class BingoLogHandler extends AbstractHandler {
    private Gson gson = new Gson();
    private Pattern pattern = Pattern.compile("^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) (?<ssid>\\d+) *(?<level>\\w)+ *\\[(?<class>.*?)\\] *\\(.*?\\) *(?<json>\\{.*\\})?");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    @Inject
    private Logger logger;

    @Inject
    private IActionService actionService;

    @Override
    protected void onInitialize() {
        this.setAction(Actions.BingoLog);
    }

    @Override
    protected void onExecute(IContext context) {

        this.actionService.execute(Actions.GeneralFilter, context);

        Boolean filted = context.getVariable(Constants.FILTERED_FLAG);
        if (filted != null && filted) {
            return;
        }
        String input = context.getInput();
        Matcher matcher = pattern.matcher(input);
        String jsonString = null;
        String recordTimeString = null;
        String recordSIDString = null;
        while (matcher.find()) {
            jsonString = matcher.group("json");
            recordTimeString = matcher.group("date");
            recordSIDString = matcher.group("ssid");
        }
        Date recordTime = null;
        try {
            recordTime = this.dateFormat.parse(recordTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        long recordSID = Long.parseLong(recordSIDString);

        if (jsonString != null && !jsonString.trim().isEmpty()) {
            JsonObject json = this.gson.fromJson(jsonString, JsonObject.class);
            context.setVariable("json", json);
            context.setVariable("record_time", recordTime);
            context.setVariable("record_qsid", recordSID);
            this.actionService.execute(Actions.GeneralJson, context);


            this.actionService.execute(Actions.GeneralProps, context);

        } else {
            context.setInvalid("record_time", recordTime);
            context.setInvalid("record_qsid", recordSID);
            context.setInvalid("input", context.getInput());
        }
    }
}
