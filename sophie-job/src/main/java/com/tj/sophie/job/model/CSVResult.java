package com.tj.sophie.job.model;

import com.tj.sophie.guice.Binding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evan.chiu on 2015/6/16.
 */
@Binding(from = ICSVResult.class, to = CSVResult.class)
public class CSVResult extends AbstractCSVResult implements ICSVResult{
    private boolean initialized = false;
    private List<String> extractCSVList;

    @Override
    protected void onInitialize() {
        extractCSVList = new ArrayList<>();
    }

    @Override
    public List<String> getExtractCSVList() {
        return extractCSVList;
    }

    @Override
    public void setExtractCSVList(List<String> extractCSVList) {
        this.extractCSVList = extractCSVList;
    }

}
