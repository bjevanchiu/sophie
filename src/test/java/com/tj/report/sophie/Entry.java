package com.tj.report.sophie;

/**
 * Created by mbp on 6/2/15.
 */
public class Entry {
    public static void main(String[] args) {

        // TODO 有个特殊的初始化类处理Handler注册
        IVerbService verbService = new VerbService();
        IHandler handler = new GetEventHandler();
        verbService.register(handler);




        // 入口

        VerbId verbId = new VerbId();
        verbId.setCategory("log");
        verbId.setId("process_executed");
        Context context = new Context();
        context.put("line", "xxxxxxxxx");
        verbService.execute(verbId, context);

        co



    }
}
