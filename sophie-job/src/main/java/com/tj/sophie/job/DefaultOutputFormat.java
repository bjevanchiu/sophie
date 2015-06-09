package com.tj.sophie.job;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;


public class DefaultOutputFormat extends MultipleOutputFormat<Text, Text> {
    @Override
    protected String generateFileNameForKeyValue(Text key, Text value,
                                                 Configuration conf) {
        return key.toString();
    }
}