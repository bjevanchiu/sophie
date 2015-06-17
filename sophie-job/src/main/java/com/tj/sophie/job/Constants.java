package com.tj.sophie.job;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.hadoop.fs.Path;

import java.util.*;

/**
 * Created by mbp on 6/8/15.
 */
public class Constants {
    public static final String JARS = "sophie-jars";
    public static final String JOB_NAME = "sophie-configuration-name";

    public static final String JOB_HDFS_CACHE_ROOT = "/job_cache";
    public static final String JOB_LOCAL_CACHE_ROOT = "/tmp/hadoop/job";


    public final class Keys {
        public static final String EVENTS = "Events";
        public static final String PROPS = "Props";
        public static final String CSVLIST = "CSVList";
    }

    public final class Variables {
        public static final String CONTENT_TYPE = "content_type";
        public static final String DELIVERS = "delivers";
        public static final String ORIGIN_JSON = "origin_json";
        public static final String FILTERS = "filters";
    }

    public static Path getHdfsCachePath(Path local) {
        return new Path(new Path(Constants.JOB_HDFS_CACHE_ROOT), local.getName());
    }

    public static Path getLocalCachePath(Path hdfs) {
        return new Path(new Path(Constants.JOB_LOCAL_CACHE_ROOT), hdfs.getName());
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 255; i > 0; i--) {
            map.put(String.valueOf(i), String.valueOf(i));
        }
        map = sortMapByKey(map);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(String.format("key %s value %s", entry.getKey(), entry.getValue()));
        }
        Gson gson = new Gson();
        String jsonString = gson.toJson(map, new TypeToken<Map<String, String>>() {
        }.getType());
        System.out.println(jsonString);
    }

    private static Map<String, String> sortMapByKey(Map<String, String> map) {
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);


        Map<String, String> result = new HashMap<>();
        for (String key : keys) {
            result.put(key, map.get(key));
        }
        return result;
    }
}
