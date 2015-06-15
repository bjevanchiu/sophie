package com.tj.sophie.job.model;

import java.util.*;

/**
 * Created by evan.chiu on 2015/6/16.
 */
public class CSVResult implements ICSVResult{
    private boolean initialized = false;
    private List<String> extractCSVList;
    @Override
    public void initialize() {
        if (this.initialized) {
            return;
        }
        extractCSVList = new ArrayList<>();
        this.initialized = true;
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
