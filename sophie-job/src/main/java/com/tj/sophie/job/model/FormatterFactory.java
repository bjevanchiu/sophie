package com.tj.sophie.job.model;

import com.google.inject.Inject;
import com.tj.sophie.guice.Formatter;
import com.tj.sophie.job.Container;
import com.tj.sophie.job.ReflectionUtil;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by evan.chiu on 2015/6/16.
 */
public class FormatterFactory {
    @Inject
    private Logger logger;
    private static FormatterFactory ourInstance = new FormatterFactory();
    private static Map<String, ICSVFormatter> formattersMap = new HashMap<>();

    public static FormatterFactory getInstance() {
        return ourInstance;
    }

    private FormatterFactory() {
        this.loadFormatter();
    }

    public void loadFormatter(){
        for (Class<?> clazz : Container.getInstance().getLoadedClasses()) {
            Formatter formatter = ReflectionUtil.findAnnotation(Formatter.class, clazz);
            if (formatter != null) {
                logger.info(String.format("Initialize Formatter: %s", clazz.getName()));
                try {
                    formattersMap.put(clazz.getAnnotation(Formatter.class).key(), (ICSVFormatter) clazz.newInstance());
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
