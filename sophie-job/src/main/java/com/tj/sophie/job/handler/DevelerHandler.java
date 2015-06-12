package com.tj.sophie.job.handler;


import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
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
	
	private Gson gson = new Gson();
	
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
		
		JsonObject requestObj = null;
		JsonObject responseObj = null;
		
		boolean isVersion = true;
		String input = context.getInput();
		if(input.contains("\"version\"")){
			requestObj = deliverJsonObj.get("request").getAsJsonObject();
			responseObj = deliverJsonObj.get("response").getAsJsonObject();
		}else{
			String request = deliverJsonObj.get("request").getAsString();
			String response =  deliverJsonObj.get("response").getAsString();
			requestObj = gson.fromJson(request, JsonObject.class);
			responseObj = gson.fromJson(response, JsonObject.class);
			isVersion = false;
		}
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
					JsonObject defendTailJsonObj = null;
					if(isVersion){
						defendTailJsonObj = deliverJsonObj.get("defend_detail").getAsJsonObject();
					}else{
						String defend_detail = deliverJsonObj.get("defend_detail").getAsString();
						defendTailJsonObj = gson.fromJson(defend_detail, JsonObject.class);
					}
					solutionObj.add("defend_detail", defendTailJsonObj);
				}
				delivers.add(solutionObj);
			}
		}
		if(delivers!=null && delivers.size()>0 && delivers.get(0)!=null){
			context.getMap("result").put("delivers", delivers);
		}else{
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
}
