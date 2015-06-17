package com.tj.sophie.job.model;

/**
 * Created by evanchiu on 15/6/17.
 */
public abstract class AbstractCSVResult  implements ICSVResult {
    private boolean initialized = false;
    @Override
    public void initialize() {
        if (this.initialized) {
            return;
        }
        this.initialized = true;
    }

    protected abstract void onInitialize();

}
