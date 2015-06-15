package com.tj.sophie.job.service;

import com.google.common.base.Joiner;
import com.google.gson.JsonObject;
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

    @Override
    protected void onInitialize() {
    }

    @Override
    public <T extends AbstractCSVFormatter> T transfer(Class<T> formatter, List<JsonObject> jsonOjectList) throws ParseException, IllegalAccessException, InstantiationException {
        T csvFormatter = formatter.newInstance();
        List<String> formatCSVList = new ArrayList<>();
        for(JsonObject jsonObject : jsonOjectList){
            List<Object> values = new ArrayList<>();
            for (String column : csvFormatter.getExtractColumns()) {
                values.add(jsonObject.get(column));
            }
            String formatCSV = Joiner.on(csvFormatter.getDelimiter()).useForNull(csvFormatter.getNullString()).join(values);
            if(formatCSV != null) {
                formatCSVList.add(formatCSV);
            }
        }
        csvFormatter.setExtractCSVList(formatCSVList);
        return csvFormatter;


    }
}
