package com.tj.sophie.job;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IHandler;
import com.tj.sophie.guice.Binding;
import com.tj.sophie.guice.GuiceModule;
import com.tj.sophie.guice.Handler;
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

    public Injector getInjector() {
        return this.injector;
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

        List<AbstractModule> modules = this.loadModules(types);
        modules.add(0, mainModule);

        this.injector = Guice.createInjector(modules);

        IActionService actionService = this.injector.getInstance(IActionService.class);
        for (Class<IHandler> type : handlerTypes) {
            IHandler handler = this.injector.getInstance(type);
            logger.info(String.format("register handler %s %s", handler.getAction().getCategory(), handler.getAction().getId()));
            actionService.register(handler);
        }

    }

    public List<AbstractModule> loadModules(List<Class<?>> types) {
        Logger logger = Container.getLogger();
        List<AbstractModule> modules = new ArrayList<>();
        for (Class<?> clazz : types) {
            GuiceModule guiceModule = ReflectionUtil.findAnnotation(GuiceModule.class, clazz);
            boolean isModule = AbstractModule.class.isAssignableFrom(clazz);
            if (guiceModule != null && isModule) {
                AbstractModule module = null;
                try {
                    module = (AbstractModule) clazz.newInstance();
                    modules.add(module);
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                    logger.info(String.format("new instance $s error", module.toString()));
                }
            }
        }
        return modules;
    }
}
