package com.tj.sophie.job.handler;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.Constants;
import com.tj.sophie.job.helper.JsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mbp on 6/9/15.
 */
@Handler
public class DeliverHandler extends AbstractHandler {

    private Gson gson = new Gson();

    @Override
    protected void onInitialize() {
        this.setAction(Actions.GeneralDeliver);


    }

    @Override
    protected void onExecute(IContext context) {
        JsonObject json = context.getVariable(Constants.Variables.ORIGIN_JSON);
        if (json == null) {
            return;
        }
        if (!json.has("request") || !json.has("response")) {
            context.setError("deliver", json);
            return;
        }

        JsonObject jsonObject = this.transfer(json);

        JsonObject commonJson = JsonHelper.helloCommon(jsonObject);
        JsonObject deliverCommonJson = this.getCommon(jsonObject);

        for (Map.Entry<String, JsonElement> entry : deliverCommonJson.entrySet()) {
            JsonHelper.copyProperty(entry.getKey(), deliverCommonJson, commonJson);
        }

        JsonObject solutionListCake = jsonObject.get("response").getAsJsonObject().get("SolutionListCake")
                .getAsJsonObject();

        List<JsonObject> delivers = new ArrayList<JsonObject>();
        if (solutionListCake.has("solutionList")) {
            JsonArray solutionList = solutionListCake.get("solutionList")
                    .getAsJsonArray();
            for (JsonElement solution : solutionList) {

                JsonObject deliverObject = new JsonObject();

                deliverObject.addProperty("eventId", "deliver");

                if (solutionListCake.has("UserCake")) {
                    JsonObject userCake = solutionListCake.get("UserCake")
                            .getAsJsonObject();
                    deliverObject.add("uid", userCake.get("uid"));
                }
                if (solutionListCake.has("sid")) {
                    deliverObject.add("sid", solutionListCake.get("sid"));
                }
                this.copy(deliverObject, deliverCommonJson);
                delivers.add(deliverObject);
            }
        }
        if (delivers != null && delivers.size() > 0 && delivers.get(0) != null) {
            context.setVariable(Constants.Variables.DELIVERS, delivers);
        } else {
            context.setError("delivers", context.getInput());
        }
    }

    private JsonObject toJsonObjectByKey(JsonObject sourceJsonObject,
                                         JsonObject targetJsonObject, String[] keyArray) {
        for (String key : keyArray) {
            if (sourceJsonObject.has(key)) {
                targetJsonObject.add(key, sourceJsonObject.get(key));
            }
        }
        return targetJsonObject;
    }

    private JsonObject transfer(JsonObject jsonObject) {
        if (jsonObject.has("version")) {
            return jsonObject;
        }
        JsonObject result = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            JsonObject item = gson.fromJson(value, JsonObject.class);
            result.add(key, item);
        }
        return result;
    }

    private JsonObject getCommon(JsonObject jsonObject) {
        JsonObject result = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("request") || entry.getKey().equalsIgnoreCase("response")) {
                continue;
            }
            result.add(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private void copy(JsonObject source, JsonObject destination) {
        for (Map.Entry<String, JsonElement> entry : source.entrySet()) {
            JsonHelper.copyProperty(entry.getKey(), source, destination);
        }
    }
}
