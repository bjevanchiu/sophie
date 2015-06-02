package com.tj.sophie.job;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tj.sophie.core.IActionService;

/**
 * Created by mbp on 6/2/15.
 */
public class Container {
    private static Container ourInstance = new Container();

    public static Container getInstance() {
        return ourInstance;
    }

    private Injector injector;

    private Container() {
        this.injector = Guice.createInjector(new JobModule());
    }


    public IActionService getActionService() {
        return this.injector.getInstance(IActionService.class);
    }
}
