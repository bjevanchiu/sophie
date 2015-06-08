package com.tj.sophie.job.handler;

import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.Action;
import com.tj.sophie.core.IContext;

/**
 * Created by mbp on 6/8/15.
 */
public class HelloLogHandler extends AbstractHandler {
    @Override
    protected void onExecute(IContext context) {

    }

    @Override
    protected void onInitialize() {
        this.setAction(Action.create("main", "hello"));
    }
}
