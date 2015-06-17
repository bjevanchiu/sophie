package com.tj.sophie.job;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IContext;
import com.tj.sophie.job.helper.Helper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mbp on 6/5/15.
 */
public class MainMapper extends Mapper<Object, Text, Text, Text> {

    private Logger logger = Container.getLogger();
    private Gson gson = new Gson();
    private IActionService actionService;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        String jsonString = context.getConfiguration().get(Constants.JARS);
        List<String> typeStrings = gson.fromJson(jsonString, new TypeToken<List<String>>() {
        }.getType());
        List<Class<?>> types = new ArrayList<>();
        ClassLoader classloader = context.getConfiguration().getClassLoader();
        for (String typeString : typeStrings) {
            try {
                types.add(classloader.loadClass(typeString));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        Container.getInstance().initialize(types);
        this.actionService = Container.getInstance().getActionService();
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        super.cleanup(context);
    }

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        IActionService actionService = Container.getInstance().getActionService();

        String input = value.toString();
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        IContext ctx = new com.tj.sophie.core.Context(value.toString());

        String path = ((FileSplit) context.getInputSplit()).getPath().getName();

        ContentType contentType;

        if (path.trim().toLowerCase().indexOf("bingo") != -1) {
            ctx.setVariable(Constants.Variables.CONTENT_TYPE, ContentType.BINGO);
            contentType = ContentType.BINGO;
            actionService.execute(Actions.BingoLog, ctx);
        } else if (path.trim().toLowerCase().indexOf("hello") != -1) {
            ctx.setVariable(Constants.Variables.CONTENT_TYPE, ContentType.HELLO);
            contentType = ContentType.HELLO;
            actionService.execute(Actions.HelloLog, ctx);
        } else {
            return;
        }

        if (contentType == ContentType.BINGO) {
            Boolean flag = ctx.getVariable(Constants.Variables.FILTER_FLAG);
            if (flag == null || !flag) {
                String event = ctx.getVariable(Constants.Variables.EVENT_NAME);
                if (Helper.isNullOrEmpty(event)) {
                    context.write(new Text(path + ".error"), value);
                    return;
                }
                JsonObject jsonObject = null;
                if (event.equalsIgnoreCase("active")) {
                    jsonObject = ctx.getVariable(Constants.Variables.ACTIVE);
                } else if (event.equalsIgnoreCase("solutionCanceled")) {
                    jsonObject = ctx.getVariable(Constants.Variables.CANCELED);
                } else if (event.equalsIgnoreCase("solution_executed")) {
                    jsonObject = ctx.getVariable(Constants.Variables.EXECUTED);
                } else if (event.equalsIgnoreCase("solution_executing")) {
                    jsonObject = ctx.getVariable(Constants.Variables.EXECUTING);
                }
                if (jsonObject == null) {
                    context.write(new Text(path + ".error"), value);
                }

                String text = gson.toJson(jsonObject, JsonObject.class);
                List<JsonObject> jsonObjects = new ArrayList<>();
                jsonObjects.add(jsonObject);
                ctx.setVariable(Constants.Keys.EVENTS, jsonObjects);
                context.write(new Text(path + ".events"), new Text(text));
            }
            Map<String, Object> filters = ctx.getMap(Constants.Variables.FILTERS);
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                context.write(new Text(path + ".filter"), new Text(entry.getValue().toString()));
            }

        } else if (contentType == ContentType.HELLO) {
            List<JsonObject> delivers = ctx.getVariable(Constants.Variables.DELIVERS);
            if (delivers != null) {
                ctx.setVariable(Constants.Keys.EVENTS, delivers);

                for (JsonObject deliver : delivers) {
                    String text = gson.toJson(deliver, JsonObject.class);
                    context.write(new Text(path + ".deliver"), new Text(text));
                }
                context.write(new Text(path + ".count"), new Text("1"));
            }

            Map<String, Object> filters = ctx.getMap(Constants.Variables.FILTERS);
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                context.write(new Text(path + ".filter"), new Text(entry.getValue().toString()));
            }
        }


        actionService.execute(Actions.GeneralCSV, ctx);
        Map<String, Object> csvListMap = ctx.getMap(Constants.Keys.CSVLIST);
        if (csvListMap != null && !csvListMap.isEmpty())

        {
            List<String> csvList;
            for (Map.Entry<String, Object> entry : csvListMap.entrySet()) {
                csvList = (List<String>) entry.getValue();
                for (String csvStr : csvList) {
                    context.write(new Text(entry.getKey()), new Text(csvStr));
                }
            }
            ctx.getMaps().remove(Constants.Keys.CSVLIST);
        }
    }
}
