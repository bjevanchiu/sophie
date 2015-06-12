package com.tj.sophie.job.service;

import com.tj.sophie.core.IService;
import com.tj.sophie.job.model.AbstractCSVFormatter;

import java.text.ParseException;
import java.util.Map;

/**
 * Created by mbp on 6/10/15.
 */
public interface IGeneralCSVService extends IService {
    <T extends AbstractCSVFormatter> T transfer(Class<T> formatter, Map<String, Object> result) throws ParseException, IllegalAccessException, InstantiationException;
}
