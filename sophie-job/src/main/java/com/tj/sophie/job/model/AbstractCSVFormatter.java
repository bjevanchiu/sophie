package com.tj.sophie.job.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbp on 6/4/15.
 */
public abstract class AbstractCSVFormatter implements ICSVFormatter {
    private boolean initialized = false;
    private char delimiter;
    private List<String> extractEvents = new ArrayList<>();
    private List<String> extractColumns = new ArrayList<>();
    private String nullString = "";
    private String key = "";

    @Override
    public synchronized void initialize() {
        if (this.initialized) {
            return;
        }
        this.initialized = true;
        this.onInitialize();
    }

    protected abstract void onInitialize();

    @Override
    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public List<String> getExtractEvents() {
        return extractEvents;
    }

    public void setExtractEvents(List<String> extractEvents) {
        this.extractEvents = extractEvents;
    }

    @Override
    public List<String> getExtractColumns() {
        return extractColumns;
    }

    public void setExtractColumns(List<String> extractColumns) {
        this.extractColumns = extractColumns;
    }

    @Override
    public String getNullString() {
        return nullString;
    }

    public void setNullString(String nullString) {
        this.nullString = nullString;
    }

    @Override
    public String getKey() {
        return key;
    }

    protected void setKey(String key) {
        this.key = key;
    }
}
