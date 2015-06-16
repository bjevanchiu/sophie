package com.tj.sophie.job;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IHandler;
import com.tj.sophie.guice.Binding;
import com.tj.sophie.guice.Formatter;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.model.FormatterFactory;
import com.tj.sophie.job.model.ICSVFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mbp on 6/4/15.
 */
public class Container {
    private static Container ourInstance = new Container();
    private Injector injector;
    private boolean initialized = false;
    private List<Class<?>> loadedClasses = null;

    private Container() {
    }

    public static Container getInstance() {
        return ourInstance;
    }

    public static Logger getLogger() {
        return LoggerFactory.getLogger(Container.class);
    }

    public IActionService getActionService() {
        return this.injector.getInstance(IActionService.class);
    }

    public List<Class<?>> getLoadedClasses() {
        return this.loadedClasses;
    }

    public synchronized void initialize(List<Class<?>> types) {
        if (this.initialized) {
            return;
        }
        this.initialized = true;
        this.loadedClasses = types;
        this.initializeGuice(types);
    }

    private void initializeGuice(List<Class<?>> types) {


        MainModule mainModule = new MainModule();

        Map<Class<?>, Class<?>> mapper = new HashMap<>();
        for (Class<?> clazz : types) {
            Binding binding = ReflectionUtil.findAnnotation(Binding.class, clazz);
            if (binding != null) {
                Class<?> from = binding.from();
                Class<?> to = binding.to();
                mapper.put(from, to);
            }
        }
        mainModule.initializeBinding(mapper);

        List<Class<IHandler>> handlerTypes = new ArrayList<>();
        for (Class<?> clazz : types) {
            Handler handler = ReflectionUtil.findAnnotation(Handler.class, clazz);
            if (handler != null) {
                handlerTypes.add((Class<IHandler>) clazz);
            }
        }
        mainModule.initializeHandler(handlerTypes);

        Logger logger = Container.getLogger();

        this.injector = Guice.createInjector(mainModule);
        IActionService actionService = this.injector.getInstance(IActionService.class);
        for (Class<IHandler> type : handlerTypes) {
            IHandler handler = this.injector.getInstance(type);
            logger.info(String.format("register handler %s %s", handler.getAction().getCategory(), handler.getAction().getId()));
            actionService.register(handler);
        }

        FormatterFactory formatterFactory = FormatterFactory.getInstance();
        List<ICSVFormatter> formatters = new ArrayList<>();
        for (Class<?> clazz : types) {
            Formatter formatter = ReflectionUtil.findAnnotation(Formatter.class, clazz);
            if (formatter != null) {
                logger.info(String.format("Initialize Formatter: %s", clazz.getName()));
                formatters.add(this.injector.getInstance((Class<ICSVFormatter>) clazz));
            }
        }
        formatterFactory.loadFormatter(formatters);
    }
}
