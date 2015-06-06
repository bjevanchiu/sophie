package com.tj.sophie.job;


import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by mbp on 6/5/15.
 */
public class MainJob {
    //^(?<date>\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3}) (?<ssid>\d+) *(?<level>\w)+ *\[(?<class>.*?)\] *\(.*?\) *(?<processedFlag>processed:)?(?<json>\{.*\})
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

        Path hdfsJars = Container.uploadToHDFS(job.getConfiguration(), job.getJar());

        job.getConfiguration().set(Container.JARS, hdfsJars.toString());
        job.getConfiguration().set(Container.JOB_NAME, UUID.randomUUID().toString());
        Path cache = Container.getJobCachePath(job.getConfiguration());

        boolean result = job.waitForCompletion(true);

        FileSystem fs = FileSystem.get(job.getConfiguration());
        fs.delete(cache, true);

        System.exit(result ? 0 : 1);
    }
}
