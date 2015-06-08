package com.tj.sophie.job.handler;

import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.Action;
import com.tj.sophie.core.IContext;

/**
 * Created by mbp on 6/8/15.
 */
public class BingoLogHandler extends AbstractHandler {
    @Override
    protected void onInitialize() {
        this.setAction(Action.create("main", "bingo"));
    }

    @Override
    protected void onExecute(IContext context) {

    }
}
