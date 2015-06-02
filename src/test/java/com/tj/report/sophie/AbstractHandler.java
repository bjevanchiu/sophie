package com.tj.report.sophie;

/**
 * Created by mbp on 6/2/15.
 */
public abstract class AbstractHandler implements IHandler {

    private VerbId verbId;
    private Level level;

    public VerbId getVerbId() {
        return this.verbId;
    }

    public void setVerbId(VerbId verbId) {
        this.verbId = verbId;
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }


    public void execute(Context context) {
        this.onExecute(context);
    }

    protected abstract void onExecute(Context context);


}
