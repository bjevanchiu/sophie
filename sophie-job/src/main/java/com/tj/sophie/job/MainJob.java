package com.tj.sophie.job;


import com.google.gson.Gson;
import com.tj.sophie.job.helper.JarFileHelper;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by mbp on 6/5/15.
 */
public class MainJob {
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String input = args[0];
        String output = args[1];
        Job job = new Job();

        job.setJarByClass(MainJob.class);
        job.setMapperClass(MainMapper.class);
        job.setReducerClass(MainReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        job.setNumReduceTasks(1);
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        JarFileHelper jarFileHelper = JarFileHelper.create(job.getJar());
        List<String> typeStrings = new ArrayList<>();
        List<Class<?>> types = jarFileHelper.getTypes();
        for (Class<?> clazz : types) {
            typeStrings.add(clazz.getName());
        }
        Gson gson = new Gson();
        String jsonString = gson.toJson(typeStrings);
        job.getConfiguration().set(Constants.JARS, jsonString);

        job.getConfiguration().set(Constants.JOB_NAME, UUID.randomUUID().toString());

        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
}
