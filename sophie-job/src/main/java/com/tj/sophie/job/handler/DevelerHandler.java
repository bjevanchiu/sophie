package com.tj.sophie.job.handler;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;

/**
 * Created by mbp on 6/9/15.
 */
@Handler
public class DevelerHandler extends AbstractHandler {
	@Override
	protected void onInitialize() {
		this.setAction(Actions.GeneralDeliver);
	}

	@Override
	protected void onExecute(IContext context) {

		JsonObject deliverJsonObj = context.getVariable("deliver");
		if (deliverJsonObj == null) {
			return;
		}
		if (!deliverJsonObj.has("request") || !deliverJsonObj.has("response")) {
			context.setError("deliver", deliverJsonObj);
			return;
		}
		JsonObject requestObj = deliverJsonObj.get("request").getAsJsonObject();
		JsonObject responseObj = deliverJsonObj.get("response")
				.getAsJsonObject();
		deliverJsonObj.remove("request");
		deliverJsonObj.remove("response");
		// 遍历循环 方案号
		String keys = "imsi1,linux_v,tag,pkgn,did,sv,imei,sucess,reqid,packageCodePath,executed_solutions,app_info,samd,x-real-ip,x-forwarded-for,aid";
		JsonObject solutionListCake = responseObj.get("SolutionListCake")
				.getAsJsonObject();

		JsonArray delivers = new JsonArray();
		if (solutionListCake.has("solutionList")) {
			JsonArray solutionList = solutionListCake.get("solutionList")
					.getAsJsonArray();
			for (JsonElement solution : solutionList) {
				JsonObject solutionObj = (JsonObject) solution;
				solutionObj.remove("resoures");
				solutionObj.remove("parameters");
				solutionObj.remove("uris");
				solutionObj.addProperty("eventId", "deliver");
				solutionObj = this.toJsonObjectByKey(requestObj, solutionObj,
						keys.split(","));
				if (solutionListCake.has("UserCake")) {
					JsonObject userCake = solutionListCake.get("UserCake")
							.getAsJsonObject();
					solutionObj.add("uid", userCake.get("uid"));
				}
				if (solutionListCake.has("sid")) {
					solutionObj.add("sid", solutionListCake.get("sid"));
				}
				if(deliverJsonObj.has("ms")){
					solutionObj.add("ms", deliverJsonObj.get("ms"));
				}
				if(deliverJsonObj.has("KnownSolution")){
					solutionObj.add("KnownSolution", deliverJsonObj.get("KnownSolution"));
				}
				if(deliverJsonObj.has("defend")){
					solutionObj.add("defend", deliverJsonObj.get("defend"));
				}
				if(deliverJsonObj.has("KnownSolutionId")){
					solutionObj.add("KnownSolutionId", deliverJsonObj.get("KnownSolutionId"));
				}
				if(deliverJsonObj.has("vm_enable")){
					solutionObj.add("vm_enable", deliverJsonObj.get("vm_enable"));
				}
				if(deliverJsonObj.has("version")){
					solutionObj.add("version", deliverJsonObj.get("version"));
				}
				if(deliverJsonObj.has("defend_detail")){
					solutionObj.add("defend_detail", deliverJsonObj.get("defend_detail"));
				}
				delivers.add(solutionObj);
			}
			context.getMap("result").put("delivers", delivers);
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
}
