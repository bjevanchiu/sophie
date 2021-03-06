package com.tj.sophie.job.service;

import com.google.gson.JsonObject;
import com.tj.sophie.core.IService;

/**
 * Created by mbp on 6/10/15.
 */
public interface IFilterService extends IService {
    boolean accept(String input);

    boolean acceptEvent(String event, JsonObject jsonObject);
}
