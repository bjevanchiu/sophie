package com.tj.sophie.core;

/**
 * Created by mbp on 6/4/15.
 */
public abstract class AbstractService implements IService {
    private boolean initialized = false;

    @Override
    public synchronized void initialize() {
        if (this.initialized) {
            return;
        }
        this.initialized = true;
        this.onInitialize();
    }

    protected abstract void onInitialize();
}
