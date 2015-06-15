package com.tj.sophie.job.handler;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.ContentType;

@Handler
public class GeneralPsHandler extends AbstractHandler {

	@Override
	protected void onInitialize() {
		this.setAction(Actions.GeneralPs);

	}

	@Override
	protected void onExecute(IContext context) {
		Integer contentType = context.getVariable("content_type");
		if (contentType.equals(ContentType.BINGO)) {
			processBingoPs(context);
		} else if (contentType.equals(ContentType.HELLO)) {
			processHelloPs(context);
		}
	}

	private void processHelloPs(IContext context) {
		JsonObject jsonObject = context.getVariable("json");
		if (jsonObject == null) {
			return;
		}
		JsonArray ps = null;
		if (jsonObject.has("ps")) {
			ps = jsonObject.getAsJsonArray("ps");
		}
		if (ps != null) {
			context.setVariable("ps", ps);
		} else {
			context.setError("ps", context.getInput());
		}
	}

	private void processBingoPs(IContext context) {
		JsonObject jsonObject = context.getVariable("json");
		if (jsonObject == null) {
			return;
		}
		String event = null;
		if (jsonObject.has("eventId")) {
			event = jsonObject.get("eventId").getAsString();
		}
		if (event != null && event.equalsIgnoreCase("ps")) {
			if (jsonObject.has("reasons")) {
				JsonObject psJson = jsonObject.getAsJsonObject("reasons");
				Set<Entry<String, JsonElement>> psSet = psJson.entrySet();
				Iterator<Entry<String, JsonElement>> it = psSet.iterator();
				JsonArray jsonArrayPs = new JsonArray();
				while (it.hasNext()) {
					JsonObject ps = new JsonObject();
					Entry<String, JsonElement> psInfo = it.next();
					String line = psInfo.getValue().getAsString();
					if(line.startsWith("USER")){//去掉ps结果的表头
						continue;
					}
					String arr[] = line.split("\\s+");
					String name = arr[arr.length - 1];
					String status = arr[arr.length - 2];
					ps.addProperty("user", arr[0]);
					ps.addProperty("pid", Integer.parseInt(arr[1]));
					ps.addProperty("ppid", Integer.parseInt(arr[2]));
					ps.addProperty("status", status);
					ps.addProperty("name", name);
					jsonArrayPs.add(ps);
				}
				context.setVariable("ps", jsonArrayPs);
			} else {
				context.setError("ps", context.getInput());
			}
		}
	}
}
