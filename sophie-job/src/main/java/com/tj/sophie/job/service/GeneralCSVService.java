package com.tj.sophie.job.service;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractService;
import com.tj.sophie.guice.Binding;
import com.tj.sophie.job.helper.Helper;
import com.tj.sophie.job.model.AbstractCSVFormatter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by mbp on 6/10/15.
 */
@Binding(from = IGeneralCSVService.class, to = GeneralCSVService.class)
public class GeneralCSVService extends AbstractService implements IGeneralCSVService {

    @Inject
    private Logger logger;

    @Override
    protected void onInitialize() {
    }

    @Override
    public <T extends AbstractCSVFormatter> T transfer(Class<T> formatter, Map<String, Object> result) throws ParseException, IllegalAccessException, InstantiationException {
        T csvFormatter = formatter.newInstance();
        Object event_name = result.get("eventId");
        if(event_name == null || Helper.isNullOrEmpty(event_name.toString())){
            return null;
        }
        else if(csvFormatter.getExtractEvents().contains(event_name)){
            List<Object> values = new ArrayList<>();
            for (String column : csvFormatter.getExtractColumns()) {
                values.add(result.get(column));
            }
            csvFormatter.setExtractCSV(Joiner.on(csvFormatter.getDelimiter()).useForNull(csvFormatter.getNullString()).join(values));
            return csvFormatter;
        }
        else{
            return null;
        }
    }
}
