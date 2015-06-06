package com.tj.sophie.job;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tj.sophie.core.Action;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IContext;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mbp on 6/5/15.
 */
public class MainMapper extends Mapper<Object, Text, Text, NullWritable> {


    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        Configuration configuration = context.getConfiguration();
        try {
            Container.getInstance().initialize(configuration);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        IActionService actionService = Container.getInstance().getActionService();
        IContext ctx = new com.tj.sophie.core.Context(value.toString());

        actionService.execute(Action.create("mainjob", "mainjob"), ctx);

        Map<String, Object> map = new HashMap<>();
        Set<Map.Entry<String, Object>> result = ctx.getResultEntries();
        for (Map.Entry<String, Object> entry : result) {
            map.put(entry.getKey(), entry.getValue());
        }
        Gson gson = new Gson();
        String jsonString = gson.toJson(map, new TypeToken<Map<String, Object>>() {
        }.getType());
        context.write(new Text(jsonString), NullWritable.get());
    }
}
