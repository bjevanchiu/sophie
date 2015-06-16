package com.tj.sophie.job;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IContext;
import org.apache.commons.lang.StringUtils;
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

        if (path.trim().toLowerCase().indexOf("bingo") != -1) {
            ctx.setVariable(Constants.Variables.CONTENT_TYPE, ContentType.BINGO);
            actionService.execute(Actions.BingoLog, ctx);
            throw new RuntimeException(input);
        } else if (path.trim().toLowerCase().indexOf("hello") != -1) {
            ctx.setVariable(Constants.Variables.CONTENT_TYPE, ContentType.HELLO);
            actionService.execute(Actions.HelloLog, ctx);
        } else {
            return;
        }


        String errorString = ctx.getError("delivers");
        if (StringUtils.isNotEmpty(errorString)) {
            context.write(new Text(path + ".error"), new Text(errorString));
        }
        String invalidString = ctx.getInvalid("hello");
        if (StringUtils.isNotEmpty(invalidString)) {
            context.write(new Text(path + ".invalid"), value);
            throw new RuntimeException(invalidString);
        }

        List<JsonObject> delivers = ctx.getVariable(Constants.Variables.DELIVERS);
        if (delivers != null) {
            ctx.setVariable(Constants.keys.EVENTS, delivers);

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


//        Map<String, Map<String, Object>> maps = ctx.getMaps();
//        Set<Map.Entry<String, Map<String, Object>>> mapEntries = maps.entrySet();
//        for (Map.Entry<String, Map<String, Object>> entry : mapEntries) {
//            String valueString = this.gson.toJson(new TreeMap(entry.getValue()), new TypeToken<Map<String, Object>>() {
//            }.getType());
//            if (!valueString.equalsIgnoreCase("{}")) {
//                context.write(new Text(entry.getKey()), new Text(valueString));
//            }
//        }

        actionService.execute(Actions.GeneralCSV, ctx);
        Map<String, Object> csvListMap = ctx.getMap(Constants.keys.CSVLIST);
        if (csvListMap != null && !csvListMap.isEmpty()) {
            List<String> csvList;
            for (Map.Entry<String, Object> entry : csvListMap.entrySet()) {
                csvList = (List<String>) entry.getValue();
                for (String csvStr : csvList) {
                    context.write(new Text(entry.getKey()), new Text(csvStr));
                }
            }
            ctx.getMaps().remove(Constants.keys.CSVLIST);
        }
    }
}
