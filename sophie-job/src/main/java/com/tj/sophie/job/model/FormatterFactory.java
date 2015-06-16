package com.tj.sophie.job.model;

import com.google.inject.Inject;
import com.tj.sophie.guice.Formatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by evan.chiu on 2015/6/16.
 */
public class FormatterFactory {
    private static FormatterFactory ourInstance = new FormatterFactory();
    private static Map<String, ICSVFormatter> formattersMap = new HashMap<>();

    public static FormatterFactory getInstance() {
        return ourInstance;
    }

    private FormatterFactory() {
    }

    public void loadFormatter(List<ICSVFormatter> formatters){
        for(ICSVFormatter formatter : formatters){
            try {
                formattersMap.put(formatter.getClass().getAnnotation(Formatter.class).key(), formatter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ICSVFormatter getFormatter(String key){
        return formattersMap.get(key);
    }


}
