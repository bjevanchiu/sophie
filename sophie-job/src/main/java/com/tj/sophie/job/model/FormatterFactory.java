package com.tj.sophie.job.model;

import com.tj.sophie.guice.Formatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by evan.chiu on 2015/6/16.
 */
public class FormatterFactory {
    private static FormatterFactory ourInstance = new FormatterFactory();
    private static Map<String, ICSVFormatter> formatters = new HashMap<>();

    public static FormatterFactory getInstance() {
        return ourInstance;
    }

    private FormatterFactory() {
    }

    public void initializeFormatter(List<Class<ICSVFormatter>> formatterTypes){
        for(Class<ICSVFormatter> formatterType : formatterTypes){
            try {
                formatters.put(formatterType.getAnnotation(Formatter.class).key(), formatterType.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ICSVFormatter getFormatter(String key){
        return formatters.get(key);
    }


}
