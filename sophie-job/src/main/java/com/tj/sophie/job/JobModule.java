package com.tj.sophie.job;

import com.google.inject.AbstractModule;
import com.tj.sophie.core.ActionService;
import com.tj.sophie.core.IActionService;

/**
 * Created by mbp on 6/2/15.
 */
public class JobModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(IActionService.class).to(ActionService.class);

    }
}
