package com.tj.sophie.job.handler;

import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.Action;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;

/**
 * Created by mbp on 6/8/15.
 */
@Handler
public class HelloLogHandler extends AbstractHandler {
    @Override
    protected void onInitialize() {
        this.setAction(Action.create("main", "hello"));
    }

    @Override
    protected void onExecute(IContext context) {

    }
}
