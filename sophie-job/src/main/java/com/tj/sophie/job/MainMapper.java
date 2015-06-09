package com.tj.sophie.job;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IContext;
import com.tj.sophie.job.helper.HadoopHelper;
import com.tj.sophie.job.helper.JarFileHelper;
import com.tj.sophie.job.service.Actions;
import org.apache.hadoop.fs.Path;
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
    private IActionService actionService;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        super.setup(context);
        String pathString = context.getConfiguration().get(Constants.JARS);
        Path hdfs = new Path(pathString);
        Path local = Constants.getLocalCachePath(hdfs);
        HadoopHelper helper = HadoopHelper.create(context.getConfiguration());
        helper.copyHdfsToLocal(hdfs, local);
        try {
            JarFileHelper jarHelper = JarFileHelper.create(local.toString());
            Container.getInstance().initialize(jarHelper.getTypes());
            this.actionService = Container.getInstance().getActionService();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
            ctx.setVariable("content_type", ContentType.BINGO);
            actionService.execute(Actions.BingoLog, ctx);
        } else if (path.trim().toLowerCase().indexOf("hello") != -1) {
            ctx.setVariable("content_type", ContentType.HELLO);
            actionService.execute(Actions.HelloLog, ctx);
        } else {
            return;
        }
        String errorString = this.gson.toJson(ctx.getErrorMap(), new TypeToken<Map<String, Object>>() {
        }.getType());
        context.write(new Text("error" + errorString), NullWritable.get());

        String resultString = this.gson.toJson(ctx.getResultMap(), new TypeToken<Map<String, Object>>() {
        }.getType());
        context.write(new Text("result" + resultString), NullWritable.get());

        String invalidString = this.gson.toJson(ctx.getInvalidMap(), new TypeToken<Map<String, Object>>() {
        }.getType());
        context.write(new Text("invalid" + invalidString), NullWritable.get());
    }
}
