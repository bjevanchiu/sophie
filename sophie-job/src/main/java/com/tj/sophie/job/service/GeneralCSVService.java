package com.tj.sophie.job.service;

import com.google.common.base.Joiner;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.tj.sophie.core.AbstractService;
import com.tj.sophie.guice.Binding;
import com.tj.sophie.job.model.FormatterFactory;
import com.tj.sophie.job.model.ICSVFormatter;
import com.tj.sophie.job.model.ICSVResult;

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

    @Override
    protected void onInitialize() {
    }

    @Override
    public ICSVResult transfer(String formatterKey, List<JsonObject> jsonOjectList) throws ParseException, IllegalAccessException, InstantiationException {
        ICSVFormatter formatter = FormatterFactory.getInstance().getFormatter(formatterKey);
        List<String> formatCSVList = new ArrayList<>();
        for(JsonObject jsonObject : jsonOjectList){
            List<Object> values = new ArrayList<>();
            for (String column : formatter.getExtractColumns()) {
                values.add(jsonObject.get(column));
            }
            String formatCSV = Joiner.on(formatter.getDelimiter()).useForNull(formatter.getNullString()).join(values);
            if(formatCSV != null) {
                formatCSVList.add(formatCSV);
            }
        }
        result.setExtractCSVList(formatCSVList);
        return result;
    }
}
