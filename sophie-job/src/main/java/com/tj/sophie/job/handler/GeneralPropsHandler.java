package com.tj.sophie.job.handler;

import com.google.gson.JsonObject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.ContentType;

@Handler
public class GeneralPropsHandler extends AbstractHandler {
	@Override
	protected void onInitialize() {
		this.setAction(Actions.GeneralProps);
	}

	@Override
	protected void onExecute(IContext context) {
		Integer contentType = context.getVariable("content_type");
		if (contentType.equals(ContentType.BINGO)) {
			ProcessBingoProps(context);
		} else if (contentType.equals(ContentType.HELLO)) {
			processHelloProps(context);
		}
	}

	private void processHelloProps(IContext context) {
		JsonObject jsonObject = context.getVariable("json");
		if (jsonObject == null) {
			return;
		}

		JsonObject request = null;
		if (jsonObject.has("request")) {
			request = jsonObject.getAsJsonObject("request");
		}
		if (request == null) {
			return;
		}

		JsonObject props = null;
		if (request.has("props")) {
			props = request.getAsJsonObject("props");
		}
		if (props != null) {
			context.getMap("result").put("props", props);
		} else {
			context.setError("props", context.getInput());
		}
	}

	private void ProcessBingoProps(IContext context) {
		JsonObject jsonObject = context.getVariable("json");
		if (jsonObject == null) {
			return;
		}

		String event = null;
		if (jsonObject.has("eventId")) {
			event = jsonObject.get("eventId").getAsString();
		}

		if (event != null && event.equalsIgnoreCase("props")) {
			if (jsonObject.has("reasons")) {
				JsonObject reasons = jsonObject.getAsJsonObject("reasons");
				if (reasons.entrySet().isEmpty()) {
					context.setError("props", context.getInput());
				} else {
					context.getMap("result").put("props", reasons);
				}
			} else {
				context.setError("props", context.getInput());
			}
		}
	}
}
