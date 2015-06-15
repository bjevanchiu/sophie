package com.tj.sophie.job.service;

import com.google.gson.JsonObject;
import com.tj.sophie.core.IService;
import com.tj.sophie.job.model.AbstractCSVFormatter;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by mbp on 6/10/15.
 */
public interface IGeneralCSVService extends IService {
    <T extends AbstractCSVFormatter> T transfer(Class<T> formatter, List<JsonObject> jsonOjectList) throws ParseException, IllegalAccessException, InstantiationException;
}
