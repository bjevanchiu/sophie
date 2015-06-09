package com.tj.sophie.job;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.tj.sophie.core.IHandler;
import com.tj.sophie.core.IInitializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mbp on 6/4/15.
 */
class MainModule extends AbstractModule {

    private Map<Class<?>, Class<?>> mapper = new HashMap<>();
    private List<Class<IHandler>> handlerTypes = new ArrayList<>();

    public void initializeBinding(Map<Class<?>, Class<?>> mapper) {
        this.mapper = mapper;
    }

    public void initializeHandler(List<Class<IHandler>> handlerTypes) {
        this.handlerTypes = handlerTypes;
    }

    @Override
    protected void configure() {
        Logger logger = Container.getLogger();
        for (Map.Entry<Class<?>, Class<?>> entry : mapper.entrySet()) {
            Class<?> fromclazz = entry.getKey();
            Class<?> toclazz = entry.getValue();

            Key<Object> from = (Key<Object>) Key.get(fromclazz);
            Key<Object> to = (Key<Object>) Key.get(toclazz);

            logger.info(String.format("bind from %s to %s", fromclazz.toString(), toclazz.toString()));

            this.bind(from).to(to);
        }
        for (Class<IHandler> handler : this.handlerTypes) {
            logger.info(String.format("bind handler %s", handler.toString()));
            this.bind(handler).in(Singleton.class);
        }
        this.bind(Logger.class).toInstance(LoggerFactory.getLogger(Container.class));
        this.bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                encounter.register(new InjectionListener<I>() {
                    @Override
                    public void afterInjection(I injectee) {
                        if (injectee instanceof IInitializable) {
                            ((IInitializable) injectee).initialize();
                        }
                    }
                });
            }
        });
    }
}
