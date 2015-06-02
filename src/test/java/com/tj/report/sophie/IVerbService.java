package com.tj.report.sophie;

/**
 * Created by mbp on 6/2/15.
 */

/**
 * 标识消息处理服务.
 */
public interface IVerbService {

    /**
     * 注册消息处理器.
     *
     * @param handler 消息处理器实例.
     */
    void register(IHandler handler);

    /**
     * 注销消息处理器.
     *
     * @param handler 消息处理器实例.
     */
    void unregister(IHandler handler);


    /**
     * 处理消息.
     *
     * @param verbId  消息Id
     * @param context 上下文.
     */
    void execute(VerbId verbId, Context context);
}
