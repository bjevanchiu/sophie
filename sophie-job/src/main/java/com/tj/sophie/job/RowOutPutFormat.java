package com.hadoop.test.multiple;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;

import com.hadoop.common.MultipleOutputFormat;


public class RowOutPutFormat extends MultipleOutputFormat<Text, Text> {
	@Override
	protected String generateFileNameForKeyValue(Text key, Text value,
			Configuration conf) {
		return key.toString();
	}
}