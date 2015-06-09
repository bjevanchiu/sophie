package com.tj.sophie.job.handler;

import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.service.Actions;

/**
 * Created by mbp on 6/9/15.
 */
@Handler
public class GeneralFilterHandler extends AbstractHandler {
    @Override
    protected void onInitialize() {
        this.setAction(Actions.GeneralFilter);
    }

    @Override
    protected void onExecute(IContext context) {
        // 根据输入设置是否进行后续处理
    }
}
