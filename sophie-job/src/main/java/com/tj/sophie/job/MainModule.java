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
import com.tj.sophie.core.IService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mbp on 6/4/15.
 */
public class MainModule extends AbstractModule {

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
        for (Map.Entry<Class<?>, Class<?>> entry : mapper.entrySet()) {
            Class<?> fromclazz = entry.getKey();
            Class<?> toclazz = entry.getValue();

            Key<Object> from = (Key<Object>) Key.get(fromclazz);
            Key<Object> to = (Key<Object>) Key.get(toclazz);

            this.bind(from).to(to);
        }
        for (Class<IHandler> handler : this.handlerTypes) {
            this.bind(handler).in(Singleton.class);
        }
        this.bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                encounter.register(new InjectionListener<I>() {
                    @Override
                    public void afterInjection(I injectee) {
                        if (injectee instanceof IService) {
                            ((IService) injectee).initialize();
                        }
                    }
                });
            }
        });
    }
}