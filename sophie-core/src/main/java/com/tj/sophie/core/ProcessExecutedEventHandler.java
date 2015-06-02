package com.tj.sophie.core;

import com.google.gson.JsonObject;

import javax.inject.Inject;

/**
 * Created by mbp on 6/2/15.
 */
public class ProcessExecutedEventHandler extends AbstractHandler {
    @Inject
    private IVerbService verbService;

    @Override
    protected void onExecute(Context context) {

        JsonObject json = null;

        VerbId verbId = new VerbId();
        verbId.setCategory("log");
        verbId.setId("event");
        verbService.execute(verbId, context);
        json.addProperty("event", (String) context.get("event"));

        verbId.setCategory("log");
        verbId.setId("device");
        verbService.execute(verbId, context);
        json.addProperty("device", (String) context.get("device"));


        // 更多处理过程



        context.put("executed",json);


    }
}
