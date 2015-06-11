package com.tj.sophie.job.service;

import com.google.gson.JsonObject;
import com.tj.sophie.core.IService;
import com.tj.sophie.job.ContentType;

import java.text.ParseException;

/**
 * Created by mbp on 6/10/15.
 */
public interface IGeneralJsonService extends IService {
    JsonObject parse(ContentType contentType, String input) throws ParseException;
}
