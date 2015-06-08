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
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * Created by mbp on 6/5/15.
 */
public class MainMapper extends Mapper<Object, Text, Text, NullWritable> {

    private Logger logger = Container.getLogger();
    private Gson gson = new Gson();

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

        String path = ((FileSplit) context.getInputSplit()).getPath().getName();

        if (path.trim().toLowerCase().indexOf("bingo") != -1) {
            ctx.setVariable("type", "bingo");
            actionService.execute(Action.create("main", "bingo"), ctx);
        } else if (path.trim().toLowerCase().indexOf("hello") != -1) {
            ctx.setVariable("type", "hello");
            actionService.execute(Action.create("main", "hello"), ctx);
        }


        String errorString = this.gson.toJson(ctx.getErrorMap(), new TypeToken<Map<String, Object>>() {
        }.getType());
        this.logger.info(String.format("errorString %s", errorString));
        context.write(new Text("error" + errorString), NullWritable.get());

        String resultString = this.gson.toJson(ctx.getResultMap(), new TypeToken<Map<String, Object>>() {
        }.getType());
        this.logger.info(String.format("resultString %s", resultString));
        context.write(new Text("result" + resultString), NullWritable.get());

        String invalidString = this.gson.toJson(ctx.getInvalidMap(), new TypeToken<Map<String, Object>>() {
        }.getType());
        this.logger.info(String.format("invalidString %s", invalidString));
        context.write(new Text("invalid" + invalidString), NullWritable.get());
    }
}
