package com.tj.sophie.job.handler;

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
        JsonObject deliverObj = context.getVariable("deliver");
        if (deliverObj == null) {
            return;
        }
        if(!deliverObj.has("request") || !deliverObj.has("response")){
        	context.setError("deliver", deliverObj);
        	return ;
        }
        JsonObject requestObj = deliverObj.get("request").getAsJsonObject();
        JsonObject responseObj = deliverObj.get("response").getAsJsonObject();
        deliverObj.remove("request");
        deliverObj.remove("response");
        
        deliverObj.addProperty("eventId", "deliver");
        //TODO
        String keys="imsi1,linux_v,tag,pkgn,did,sv,imei,uid,sucess,reqid,packageCodePath,executed_solutions,app_info,samd,aid";
        deliverObj = this.toJsonObjectByKey(requestObj, deliverObj, keys.split(","));
        
        // 遍历循环 方案号 
        JsonObject solutionListCake = responseObj.get("SolutionListCake").getAsJsonObject();
        if(solutionListCake.has("UserCake")){
        	JsonObject userCake = solutionListCake.get("UserCake").getAsJsonObject();
        	deliverObj.add("uid", userCake.get("uid"));
        }
    	if(solutionListCake.has("sid")){
    		deliverObj.add("sid", solutionListCake.get("sid"));
    	}
    	if(solutionListCake.has("solutionList")){
    		 JsonArray  solutionList = solutionListCake.get("solutionList").getAsJsonArray();
    		 for(JsonElement solution : solutionList){
    			 JsonObject solutionObj = (JsonObject)solution;
    			 deliverObj.add("solution_id", solutionObj.get("id"));
    			 context.getMap("result").put(solutionObj.get("id").getAsString(), deliverObj);
    	     }
    	}
        
    }
    
    
    private JsonObject toJsonObjectByKey(JsonObject sourceJsonObject,JsonObject targetJsonObject,String[] keyArray){
    	for(String key : keyArray){
    		if(sourceJsonObject.has(key)){
    			targetJsonObject.add(key, sourceJsonObject.get(key));
            }
    	}
    	return targetJsonObject;
    }
}
