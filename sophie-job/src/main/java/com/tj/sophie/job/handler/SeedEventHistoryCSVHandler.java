package com.tj.sophie.job.handler;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.model.ContextKeys;
import com.tj.sophie.job.model.SeedEventHistoryCSVFormatter;
import com.tj.sophie.job.service.IGeneralCSVService;

import java.util.List;

/**
 * Created by mbp on 11/5/15.
 */
@Handler
public class SeedEventHistoryCSVHandler extends AbstractHandler {

    @Inject
    private IGeneralCSVService csvService;

    @Override
    protected void onInitialize() {
        this.setAction(Actions.CSVAdaptor);
    }

    @Override
    protected void onExecute(IContext context) {
        List<JsonObject> eventJsonObjects = context.getVariable(ContextKeys.EVENTS);
        if(eventJsonObjects == null || eventJsonObjects.isEmpty()){
            return;
        }
        else{
            try {
                SeedEventHistoryCSVFormatter seedEventHistoryCSVFormatter = csvService.transfer(SeedEventHistoryCSVFormatter.class, eventJsonObjects);
                if(seedEventHistoryCSVFormatter != null && !seedEventHistoryCSVFormatter.getExtractCSVList().isEmpty()){
                    context.getMap(ContextKeys.CSVLIST).put(ContextKeys.EVENTS, seedEventHistoryCSVFormatter.getExtractCSVList());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }
}
