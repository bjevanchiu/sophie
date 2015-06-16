package com.tj.sophie.job.helper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tj.sophie.job.ContentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mbp on 6/16/15.
 */
public final class JsonHelper {
    private static Gson gson = new Gson();

    private static List<String> bingoKeys = new ArrayList<>();

    private static List<String> helloKeys = new ArrayList<>();


    static {
        bingoKeys.add("_id");
        bingoKeys.add("aid");
        bingoKeys.add("apil");
        bingoKeys.add("appid");
        bingoKeys.add("brand");
        bingoKeys.add("did");
        bingoKeys.add("eventId");
        bingoKeys.add("imei1");
        bingoKeys.add("imsi1");
        bingoKeys.add("linux_v");
        bingoKeys.add("model");
        bingoKeys.add("pkgn");
        bingoKeys.add("sv");
        bingoKeys.add("tag");
        bingoKeys.add("timeStamp");


        helloKeys.add("aid");
        helloKeys.add("did");
        helloKeys.add("host");
        helloKeys.add("imei1");
        helloKeys.add("imsi1");
        helloKeys.add("linux_v");
        helloKeys.add("packageCodePath");
        helloKeys.add("pkgn");
        helloKeys.add("reqid");
        helloKeys.add("samd");
        helloKeys.add("sucess");
        helloKeys.add("sv");
        helloKeys.add("tag");
        helloKeys.add("uid");
        helloKeys.add("x-forwarded-for");
        helloKeys.add("x-real-ip");
    }

    public static JsonObject common(ContentType contentType, JsonObject jsonObject) {
        if (contentType == ContentType.BINGO) {
            return bingoCommon(jsonObject);
        } else {
            return helloCommon(jsonObject);
        }
    }


    public static JsonObject bingoCommon(JsonObject jsonObject) {
        JsonObject result = new JsonObject();
        for (String key : bingoKeys) {
            if (jsonObject.has(key)) {
                result.add(key, jsonObject.get(key));
            }
        }
        return result;
    }

    public static JsonObject helloCommon(JsonObject jsonObject) {
        JsonObject result = new JsonObject();
        if (jsonObject.has("request")) {
            JsonObject request = jsonObject.get("request").getAsJsonObject();
            for (String key : helloKeys) {
                if (request.has(key)) {
                    result.add(key, request.get(key));
                }
            }
        }
        return result;
    }

    public static JsonObject join(JsonObject x, JsonObject y) {
        return join(x, y, true);
    }

    public static JsonObject join(JsonObject x, JsonObject y, boolean replace) {
        String jsonString = gson.toJson(x, JsonObject.class);
        JsonObject result = gson.fromJson(jsonString, JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : y.entrySet()) {
            String key = entry.getKey();
            if (result.has(key)) {
                if (replace) {
                    result.add(key, entry.getValue());
                }
            } else {
                result.add(key, entry.getValue());
            }
        }
        return result;
    }

    public static boolean copyProperty(String name, JsonObject source, JsonObject destination) {
        if (source.has(name)) {
            destination.add(name, source.get(name));
            return true;
        }
        return false;
    }
}
