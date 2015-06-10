package com.tj.sophie.job.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tj.sophie.core.AbstractService;
import com.tj.sophie.guice.Binding;
import com.tj.sophie.job.ContentType;
import com.tj.sophie.job.helper.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mbp on 6/10/15.
 */
@Binding(from = IGeneralJsonService.class, to = GeneralJsonService.class)
public class GeneralJsonService extends AbstractService implements IGeneralJsonService {
    private Pattern bingoPattern = Pattern.compile("^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) (?<qsid>\\d+) *(?<level>\\w)+ *\\[(?<class>.*?)\\] *\\(.*?\\) *(?<json>\\{.*\\})?");
    private Pattern helloPattern = Pattern.compile("^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) (?<qsid>\\d+) *(?<level>\\w)+ *\\[(?<class>.*?)\\] *\\(.*?\\) *processed:(?<json>\\{.*\\})?");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    private Gson gson = new Gson();

    @Override
    protected void onInitialize() {

    }

    @Override
    public JsonObject parse(ContentType contentType, String input) throws ParseException {
        if (Helper.isNullOrEmpty(input)) {
            return null;
        }
        Matcher matcher = null;
        if (contentType == ContentType.BINGO) {
            matcher = bingoPattern.matcher(input);
        } else if (contentType == ContentType.HELLO) {
            matcher = helloPattern.matcher(input);
        }
        while (matcher.find()) {
            String dateString = matcher.group("date");
            String qsidString = matcher.group("qsid");
            String jsonString = matcher.group("json");
            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
            jsonObject.addProperty("record_time", dateString);
            jsonObject.addProperty("record_qsid", qsidString);
            return jsonObject;
        }
        return null;
    }
}
