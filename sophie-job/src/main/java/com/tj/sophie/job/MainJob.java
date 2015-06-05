package com.tj.sophie.job;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by mbp on 6/5/15.
 */
public class MainJob {
    //^(?<date>\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3}) (?<ssid>\d+) *(?<level>\w)+ *\[(?<class>.*?)\] *\(.*?\) *(?<processedFlag>processed:)?(?<json>\{.*\})
    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        System.out.println("--------------------");


        Configuration conf = new Configuration();
        Job job = new Job(conf, args[2]);

        Logger.debug("hello");
        job.setJarByClass(MainJob.class);
        job.setMapperClass(MainMapper.class);
        job.setReducerClass(MainReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        job.setNumReduceTasks(1);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));


        Logger.debug(String.format("job jar %s", job.getJar()));
        Logger.debug(String.format("instance is %s", conf.getClassLoader().loadClass("com.tj.sophie.job.MainJob")));
        Logger.debug(String.format("path %s", conf.getClassLoader().getResource("")));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
