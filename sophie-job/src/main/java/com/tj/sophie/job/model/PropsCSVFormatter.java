package com.tj.sophie.job.model;

import com.tj.sophie.guice.Binding;
import com.tj.sophie.job.export.Formatter;
import com.tj.sophie.job.Constants;

import java.util.Arrays;

/**
 * Created by mbp on 6/4/15.
 */
@Binding(from = ICSVFormatter.class, to = PropsCSVFormatter.class)
@Formatter(key = Constants.Keys.PROPS)
public class PropsCSVFormatter extends AbstractCSVFormatter implements ICSVFormatter{

    @Override
    protected void onInitialize() {
        this.setKey(Constants.Keys.PROPS);
        this.setDelimiter('|');
        this.setNullString("\\N");
        this.setExtractEvents(Arrays.asList("props"));
        this.setExtractColumns(Arrays.asList("_id","imsi1","model","linux_v","tag","pkgn","brand","appid","did","sv",
                "imei1","apil","time_stamp"));
    }
}
