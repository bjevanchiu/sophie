package com.tj.sophie.handler;

import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.Action;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;

/**
 * Created by mbp on 6/3/15.
 */
@Handler
public class MainHandler extends AbstractHandler {

    public MainHandler() {
        this.setAction(Action.create("main", "main"));
    }

    @Override
    protected void onExecute(IContext context) {
    }
}
