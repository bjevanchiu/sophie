package com.tj.sophie.job;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tj.sophie.core.IActionService;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mbp on 6/2/15.
 */
public class MainMapper extends Mapper<Object, Text, Text, NullWritable> {
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        IActionService actionService = Container.getInstance().getActionService();

        com.tj.sophie.core.Context ctx = null;
        ctx = new com.tj.sophie.core.Context(value.toString());

        actionService.execute("main", "main", ctx);

        Set<Map.Entry<String, Object>> entries = ctx.getResultEntries();
        Map<String, Object> map = new HashMap<>();

        for (Map.Entry<String, Object> item : entries) {
            map.put(item.getKey(), item.getValue());
        }

        Gson gson = new Gson();
        String json = gson.toJson(map, new TypeToken<Map<String, Object>>() {
        }.getType());

        context.write(new Text(json), NullWritable.get());

    }
}
