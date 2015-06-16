package com.tj.sophie.job.model;

import com.tj.sophie.guice.Formatter;
import com.tj.sophie.job.Constants;

import java.util.Arrays;

/**
 * Created by mbp on 6/4/15.
 */
@Formatter(key = Constants.keys.PROPS)
public class PropsCSVFormatter extends AbstractCSVFormatter implements ICSVFormatter{

    @Override
    protected void onInitialize() {
        this.setKey("props_csv");
        this.setDelimiter('|');
        this.setNullString("\\N");
        this.setExtractEvents(Arrays.asList("props"));
        this.setExtractColumns(Arrays.asList("_id,imsi1,model,linux_v,tag,pkgn,brand,appid,did,sv,imei1,apil,time_stamp"));
    }
}
