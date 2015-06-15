package com.tj.sophie.job;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xml.internal.utils.StringComparable;
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

    public static final String FILTERED_FLAG = "filted_flag";

    public final class keys{
        public static final String EVENTS = "events";
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
