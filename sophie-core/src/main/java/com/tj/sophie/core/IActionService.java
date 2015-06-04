package com.tj.sophie.core; /**
 * Created by mbp on 6/2/15.
 */

/**
 * 标识消息处理服务.
 */
public interface IActionService {

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
     * @param action  消息Id
     * @param context 上下文.
     */
    void execute(Action action, IContext context);

    /**
     * 处理消息.
     *
     * @param category VerbId的Category值.
     * @param id       VerbId的Id值
     * @param context  上下文
     */
    void execute(String category, String id, IContext context);
}
