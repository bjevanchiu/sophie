package com.tj.sophie.job;

import com.tj.sophie.core.IActionService;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by mbp on 6/2/15.
 */
public class MainMapper extends Mapper<Object, Text, Text, NullWritable> {
    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        IActionService actionService = Container.getInstance().getActionService();

        com.tj.sophie.core.Context ctx = new com.tj.sophie.core.Context();
        ctx.set("line", value);

        try {
            actionService.execute("main", "main", ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String output = ctx.get("output");

        context.write(new Text(output), NullWritable.get());

    }
}
