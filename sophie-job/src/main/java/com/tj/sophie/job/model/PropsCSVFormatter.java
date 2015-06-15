package com.tj.sophie.job.model;

import java.util.Arrays;

/**
 * Created by mbp on 6/4/15.
 */
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
