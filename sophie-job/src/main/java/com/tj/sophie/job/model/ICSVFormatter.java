package com.tj.sophie.job.model;

import com.tj.sophie.core.IInitializable;

import java.util.List;

/**
 * Created by evanchiu on 15/6/12.
 */
public interface ICSVFormatter extends IInitializable {

    char getDelimiter();
    List<String> getExtractEvents();
    List<String> getExtractColumns();
    String getNullString();
    String getKey();
}
