package com.tj.sophie.job;

import org.apache.commons.cli.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by mbp on 6/2/15.
 */
public class MainJob {
    public static void main(String[] args) {
        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        options.addOption("i", "input", true, "输入文件");
        options.addOption("o", "output", true, "输出文件");

        Path input = null;
        Path output = null;
        try {
            if (args.length != 2) {
                throw new RuntimeException("错误: 缺少参数.");
            }
            CommandLine commandLine = parser.parse(options, args);

            if (commandLine.hasOption('i')) {
                input = new Path(commandLine.getOptionValue('i'));
            }
            if (commandLine.hasOption('o')) {
                output = new Path(commandLine.getOptionValue('o'));
            }
            doJob(input, output);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void doJob(Path input, Path output) {
        Job job = null;
        try {
            JobConf jobConf = new JobConf();
            job = new Job(jobConf);
            job.setJarByClass(MainJob.class);
            job.setMapperClass(MainMapper.class);
            job.setReducerClass(MainReducer.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(NullWritable.class);
            job.setNumReduceTasks(1);
            FileInputFormat.addInputPath(job, input);
            FileOutputFormat.setOutputPath(job, output);
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
