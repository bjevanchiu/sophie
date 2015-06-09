package com.tj.sophie.job;

import org.apache.hadoop.fs.Path;

/**
 * Created by mbp on 6/8/15.
 */
public class Constants {
    public static final String JARS = "sophie-jars";
    public static final String JOB_NAME = "sophie-configuration-name";

    public static final String JOB_HDFS_CACHE_ROOT = "/job_cache";
    public static final String JOB_LOCAL_CACHE_ROOT = "/tmp/hadoop/job";


    public static Path getHdfsCachePath(Path local) {
        return new Path(new Path(Constants.JOB_HDFS_CACHE_ROOT), local.getName());
    }

    public static Path getLocalCachePath(Path hdfs) {
        return new Path(new Path(Constants.JOB_LOCAL_CACHE_ROOT), hdfs.getName());
    }
}
