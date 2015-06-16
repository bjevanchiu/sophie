package com.tj.sophie.job.handler;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.Constants;
import com.tj.sophie.job.model.ICSVResult;
import com.tj.sophie.job.service.IGeneralCSVService;

import java.util.List;

/**
 * Created by mbp on 11/5/15.
 */
@Handler
public class GeneralCSVHandler extends AbstractHandler {

    @Inject
    private IGeneralCSVService csvService;

    @Override
    protected void onInitialize() {
        this.setAction(Actions.GeneralCSV);
    }

    @Override
    protected void onExecute(IContext context) {
        List<JsonObject> eventJsonObjects = context.getVariable(Constants.keys.EVENTS);
        if(eventJsonObjects != null && !eventJsonObjects.isEmpty()){
            try {
                ICSVResult csvResult = csvService.transfer(Constants.keys.EVENTS, eventJsonObjects);
                if(csvResult != null && !csvResult.getExtractCSVList().isEmpty()){
                    context.getMap(Constants.keys.CSVLIST).put(Constants.keys.EVENTS, csvResult.getExtractCSVList());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<JsonObject> propsJsonObjects = context.getVariable(Constants.keys.PROPS);
        if(propsJsonObjects != null && !propsJsonObjects.isEmpty()){
            try {
                ICSVResult csvResult = csvService.transfer(Constants.keys.PROPS, propsJsonObjects);
                if(csvResult != null && !csvResult.getExtractCSVList().isEmpty()){
                    context.getMap(Constants.keys.CSVLIST).put(Constants.keys.PROPS, csvResult.getExtractCSVList());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
}
