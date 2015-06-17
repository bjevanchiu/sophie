package com.tj.sophie.job.service;

import com.google.common.base.Joiner;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractService;
import com.tj.sophie.guice.Binding;
import com.tj.sophie.job.model.CSVFormatterFactory;
import com.tj.sophie.job.model.ICSVFormatter;
import com.tj.sophie.job.model.ICSVResult;
import org.slf4j.Logger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbp on 6/10/15.
 */
@Binding(from = IGeneralCSVService.class, to = GeneralCSVService.class)
public class GeneralCSVService extends AbstractService implements IGeneralCSVService {
    @Inject
    private ICSVResult result;

    @Inject
    private Logger logger;

    @Override
    protected void onInitialize() {
    }

    @Override
    public ICSVResult transfer(String formatterKey, List<JsonObject> jsonOjectList) throws ParseException, IllegalAccessException, InstantiationException {
        ICSVFormatter formatter = CSVFormatterFactory.getInstance().getFormatter(formatterKey);

        List<String> formatCSVList = new ArrayList<>();
        for(JsonObject jsonObject : jsonOjectList){
            List<String> values = new ArrayList<>();
            JsonElement jsonElement;
            String columnValue;
            for (String column : formatter.getExtractColumns()) {
                jsonElement = jsonObject.get(column);
                if(jsonElement == null){
                    columnValue = null;
                }
                else{
                    columnValue = jsonElement.getAsString();
                }
                values.add(columnValue);
            }
            String formatCSV = Joiner.on(formatter.getDelimiter()).useForNull(formatter.getNullString()).join(values);
            if(formatCSV != null) {
                logger.info(formatCSV);
                formatCSVList.add(formatCSV);
            }
        }
        result.setExtractCSVList(formatCSVList);
        return result;
    }
}
