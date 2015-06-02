package com.tj.sophie.core;

/**
 * Created by mbp on 6/2/15.
 */

/**
 * 标识处理器基类.
 */
public abstract class AbstractHandler implements IHandler {

    private Action action;
    private Level level = Level.Normal;

    /**
     * 获取一个值, 表示处理器对应的VerbId.
     *
     * @return 返回VerbId实例.
     */
    public Action getAction() {
        return this.action;
    }

    /**
     * 设置一个值, 标识处理器对应的VerbId.
     *
     * @param action VerbId实例.
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * 获取一个值, 标识处理器的优先级.
     *
     * @return
     */
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
