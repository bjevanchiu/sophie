package com.tj.sophie.job.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Constants;
import com.tj.sophie.job.service.Actions;
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
    private Pattern pattern = Pattern.compile("^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) (?<ssid>\\d+) *(?<level>\\w)+ *\\[(?<class>.*?)\\] *\\(.*?\\) *(?<json>\\{.*\\})");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SS");

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

        Boolean filted = context.getVariable(Constants.FILTED_FLAG);
        if (filted == null || filted) {
            return;
        }

        String input = context.getInput();


        Matcher matcher = pattern.matcher(input);
        String jsonString = null;
        while (matcher.find()) {
            jsonString = matcher.group("json");
            String recordTimeString = matcher.group("date");
            Date recordTime = null;
            try {
                recordTime = this.dateFormat.parse(recordTimeString);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
            String recordSIDString = matcher.group("ssid");
            long recordSID = Long.parseLong(recordSIDString);
            context.setCommon("record_time", recordTime);
            context.setCommon("record_pid", recordSID);
        }

        if (jsonString != null && !jsonString.trim().isEmpty()) {
            JsonObject json = this.gson.fromJson(jsonString, JsonObject.class);
            context.setVariable("json", json);
            this.actionService.execute(Actions.GeneralJson, context);
        } else {
            context.setInvalid("input", context.getInput());
        }
    }
}
