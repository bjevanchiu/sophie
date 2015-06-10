package com.tj.sophie.job.handler;

import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;

/**
 * Created by mbp on 6/10/15.
 */
@Handler
public class ExecutedReasonsHandler extends AbstractHandler {
    @Override
    protected void onInitialize() {
        this.setAction(Actions.ProcessReasons);
    }

    @Override
    protected void onExecute(IContext context) {

    }
}
