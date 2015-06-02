package com.tj.report.sophie;

import com.google.gson.JsonObject;

import javax.inject.Inject;

/**
 * Created by mbp on 6/2/15.
 */
public class GetDeviceHandler extends AbstractHandler {
    @Inject
    private IVerbService verbService;

    public GetDeviceHandler() {
        VerbId verbId = new VerbId();
        verbId.setCategory("log");
        verbId.setId("process_executed");
        this.setVerbId(verbId);
        this.setLevel(Level.Normal);
    }


    @Override
    protected void onExecute(Context context) {
        // TODO 处理事件类型日志

        String line = (String) context.get("line");


        JsonObject eventJson = null;
        context.put("event", eventJson);


        VerbId verbId = new VerbId();
        verbId.setCategory("存储");
        verbId.setId("保存事件");
        verbService.execute(verbId, context);
    }
}
