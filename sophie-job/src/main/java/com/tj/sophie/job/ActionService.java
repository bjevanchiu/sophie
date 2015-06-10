package com.tj.sophie.job;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tj.sophie.core.*;
import com.tj.sophie.guice.Binding;
import org.slf4j.Logger;

import java.util.*;

/**
 * Created by mbp on 6/2/15.
 */


@Singleton
@Binding(from = IActionService.class, to = ActionService.class)
public final class ActionService implements IActionService {

    @Inject
    private Logger logger;

    private Map<Action, List<IHandler>> handlers = new HashMap<>();

    public synchronized void register(IHandler handler) {
        if (handler == null) {
            throw ExceptionHelper.argumentIsNullOrEmpty("handler");
        }
        Action action = handler.getAction();
        List<IHandler> list = null;

        if (this.handlers.containsKey(handler.getAction())) {
            list = this.handlers.get(handler.getAction());

        } else {
            list = new ArrayList<>();
        }
        if (!list.contains(handler)) {
            list.add(handler);
        }
        Collections.sort(list, new Comparator<IHandler>() {
            @Override
            public int compare(IHandler o1, IHandler o2) {
                return o1.getLevel().compareTo(o2.getLevel());
            }
        });
        this.handlers.put(action, list);
    }

    public synchronized void unregister(IHandler handler) {
        if (handler == null) {
            throw ExceptionHelper.argumentIsNullOrEmpty("handler");
        }
        Action action = handler.getAction();
        List<IHandler> list = null;

        if (this.handlers.containsKey(handler.getAction())) {
            list = this.handlers.get(handler.getAction());

        } else {
            list = new ArrayList<>();
        }
        if (list.contains(handler)) {
            list.remove(handler);
        }
        Collections.sort(list, new Comparator<IHandler>() {
            @Override
            public int compare(IHandler o1, IHandler o2) {
                return o2.getLevel().compareTo(o1.getLevel());
            }
        });
        this.handlers.put(action, list);
    }

    public synchronized void execute(Action action, IContext context) {
        if (action == null) {
            throw ExceptionHelper.argumentIsNullOrEmpty("action");
        }
        if (context == null) {
            throw ExceptionHelper.argumentIsNullOrEmpty("context");
        }
        List<IHandler> list = new ArrayList<>();
        if (this.handlers.containsKey(action)) {
            list = this.handlers.get(action);
        }


        if (!list.isEmpty()) {
            for (IHandler handler : list) {
                this.logger.info(String.format("handler executing %s %s %s ",
                        handler.getAction().getCategory(), handler.getAction().getId(),
                        handler.getClass().toString()));
                handler.execute(context);
                this.logger.info(String.format("handler executed %s %s %s ",
                        handler.getAction().getCategory(), handler.getAction().getId(),
                        handler.getClass().toString()));
            }
        }
    }

    @Override
    public void execute(String category, String id, IContext context) {
        this.execute(Action.create(category, id), context);
    }


    public void printHandler(Action action) {
        if (action == null) {
            throw ExceptionHelper.argumentIsNullOrEmpty("action");
        }
        List<IHandler> list = new ArrayList<>();
        if (this.handlers.containsKey(action)) {
            list = this.handlers.get(action);
        }
        for (IHandler handler : list) {
            this.logger.info(String.format("output handler by action %s %s %s", action.getCategory(), action.getId(), handler.getClass().toString()));
        }
    }
}
