package com.tj.sophie.job.handler;

import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Actions;
import com.tj.sophie.job.model.SeedEventHistoryCSVFormatter;
import com.tj.sophie.job.service.IGeneralCSVService;

import java.util.Map;

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
        Map<String, Object> result = context.getMap("result");
        if(result == null || result.size() == 0){
            return;
        }
        try {
            SeedEventHistoryCSVFormatter seedEventHistoryCSVFormatter = csvService.transfer(SeedEventHistoryCSVFormatter.class, result);
            if(seedEventHistoryCSVFormatter != null){
                context.setError("seed_event_histories_csv", seedEventHistoryCSVFormatter.getExtractCSV());
                context.getMap("csv").put(seedEventHistoryCSVFormatter.getKey(), seedEventHistoryCSVFormatter.getExtractCSV());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
