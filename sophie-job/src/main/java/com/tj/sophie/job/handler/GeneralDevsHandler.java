package com.tj.sophie.job.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.ContentType;

@Handler
public class GeneralDevsHandler extends AbstractHandler {
	@Inject
	private org.slf4j.Logger logger;

	@Override
	protected void onInitialize() {
		this.setAction(Actions.GeneralDevs);

	}

	@Override
	protected void onExecute(IContext context) {
		Integer contentType = context.getVariable("content_type");
		if (contentType.equals(ContentType.BINGO)) {
			processBingoDevs(context);
		} else if(contentType.equals(ContentType.HELLO)){
			processHelloDevs(context);
		} 

	}

	private void processHelloDevs(IContext context) {
		JsonObject jsonObject = context.getVariable("json");
		if (jsonObject == null) {
			return;
		}
		
		JsonObject request = null;
		if(jsonObject.has("request")){
			request = jsonObject.getAsJsonObject("request");
		}
		if(request == null){
			return;
		}
		JsonArray devs = null;
		if (request.has("devs")) {
			devs = request.getAsJsonArray("devs");
		}
		if (devs != null) {
			List<JsonObject> listDev = new ArrayList<JsonObject>();
			Iterator<JsonElement> itDev = devs.iterator();
			while (itDev.hasNext()) {
				JsonObject joDev = itDev.next().getAsJsonObject();
				listDev.add(joDev);
			}
			context.getMap("result").put("devs", listDev);
		} else {
			context.setError("devs", context.getInput());
		}
	}

//	private void ProcessRubyDevs(IContext context) {
//		Pattern pattern = Pattern
//				.compile("\"devs\"=>\"(?<devs>.*?)\"},.*?(\"eventId\"=>\"devs\")");
//		String input = context.getInput();
//		Matcher matcher = pattern.matcher(input);
//		while (matcher.find()) {
//			String devsContent = matcher.group("devs");
//			String lines[] = devsContent.split("\\\\n");
//			JsonArray jsonArrayDevs = new JsonArray();
//			for (String line : lines) {
//				try {
//					JsonObject dev = new JsonObject();
//					String arr[] = line.split("\\s+");
//					String name = arr[arr.length - 1];
//					dev.addProperty("permission", arr[0]);
//					dev.addProperty("owner", arr[1]);
//					dev.addProperty("group", arr[2]);
//					dev.addProperty("createdAt", arr[arr.length - 3] + " "
//							+ arr[arr.length - 2]);
//					dev.addProperty("name", name);
//					jsonArrayDevs.add(dev);
//				} catch (Exception e) {
//					logger.debug("GeneralDevsHandler:ProcessRubyDevs::"
//							+ e.toString());
//				}
//			}
//			context.getMap("result").put("devs", jsonArrayDevs);
//		}
//	}

	private void processBingoDevs(IContext context) {
		JsonObject jsonObject = context.getVariable("json");
		if (jsonObject == null) {
			return;
		}
		String event = null;
		if (jsonObject.has("eventId")) {
			event = jsonObject.get("eventId").getAsString();
		}
		if (event != null && event.equalsIgnoreCase("devs")) {
			if (jsonObject.has("reasons")) {
				JsonObject devsJson = jsonObject.getAsJsonObject("reasons");
				String devsContent = devsJson.get("devs").getAsString();
				String lines[] = devsContent.split("\\n");
				//JsonArray jsonArrayDevs = new JsonArray();
				List<JsonObject> devList = new ArrayList<JsonObject>();
				for (String line : lines) {
					try {
						JsonObject dev = new JsonObject();
						String arr[] = line.split("\\s+");
						String name = arr[arr.length - 1];
						dev.addProperty("permission", arr[0]);
						dev.addProperty("owner", arr[1]);
						dev.addProperty("group", arr[2]);
						dev.addProperty("createdAt", arr[arr.length - 3] + " "
								+ arr[arr.length - 2]);
						dev.addProperty("name", name);
						devList.add(dev);
						//jsonArrayDevs.add(dev);
					} catch (Exception e) {
						logger.debug("GeneralDevsHandler:processBingoDevs::"
								+ e.toString());
					}
				}
				context.getMap("result").put("devs", devList);
			} else {
				context.setError("devs", context.getInput());
			}
		}
	}
}
