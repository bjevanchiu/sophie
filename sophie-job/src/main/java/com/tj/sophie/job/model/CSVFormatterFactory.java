package com.tj.sophie.job.model;

import com.tj.sophie.job.Container;
import com.tj.sophie.job.ReflectionUtil;
import com.tj.sophie.job.export.Formatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by evan.chiu on 2015/6/16.
 */
public class CSVFormatterFactory {
    private Logger logger = LoggerFactory.getLogger(CSVFormatterFactory.class);
    private static CSVFormatterFactory ourInstance = new CSVFormatterFactory();
    private static Map<String, ICSVFormatter> formattersMap;

    private CSVFormatterFactory() {
        this.loadFormatter();
    }

    public static CSVFormatterFactory getInstance() {
        return ourInstance;
    }

    public void loadFormatter(){
        formattersMap = new HashMap<>();
        for (Class<?> clazz : Container.getInstance().getLoadedClasses()) {
            Formatter formatter = ReflectionUtil.findAnnotation(Formatter.class, clazz);
            if (formatter != null) {
                logger.info(String.format("Initialize Formatter: %s", clazz.getName()));
                try {
                    formattersMap.put(clazz.getAnnotation(Formatter.class).key(), (ICSVFormatter)Container.getInstance().getInjector().getInstance(clazz));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ICSVFormatter getFormatter(String key){
        return formattersMap.get(key);
    }


}
