package com.tj.sophie.job.model;

import com.tj.sophie.core.IInitializable;

import java.util.List;

/**
 * Created by evan.chiu on 2015/6/16.
 */
public interface ICSVResult extends IInitializable {
    List<String> getExtractCSVList();
    void setExtractCSVList(List<String> extractCSVList);
}
