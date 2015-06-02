package com.tj.report.sophie;

/**
 * Created by mbp on 6/2/15.
 */

/**
 * 消息处理器接口
 */
public interface IHandler {

    /**
     * 获取一个值, 表示消息Id.
     *
     * @return 返回处理器对应的消息Id.
     */
    VerbId getVerbId();

    /**
     * 获取一个值, 标识消息处理器的优先级.
     *
     * @return 返回优先级.
     */
    Level getLevel();

    /**
     * 消息处理.
     *
     * @param context 上下文.
     */
    void execute(Context context);

}
