package com.tj.sophie.job.service;

import com.google.gson.JsonObject;
import com.tj.sophie.core.IService;
import com.tj.sophie.job.model.ICSVResult;

import java.text.ParseException;
import java.util.List;

/**
 * Created by mbp on 6/10/15.
 */
public interface IGeneralCSVService extends IService {
    ICSVResult transfer(String formatterKey, List<JsonObject> jsonOjectList) throws ParseException, IllegalAccessException, InstantiationException;
}
