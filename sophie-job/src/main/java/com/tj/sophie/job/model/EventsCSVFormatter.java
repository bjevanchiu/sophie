package com.tj.sophie.job.model;

import com.tj.sophie.guice.Binding;
import com.tj.sophie.job.Constants;
import com.tj.sophie.job.export.Formatter;

import java.util.Arrays;

/**
 * Created by mbp on 6/4/15.
 */
@Binding(from = ICSVFormatter.class, to = EventsCSVFormatter.class)
@Formatter(key = Constants.Keys.EVENTS)
public class EventsCSVFormatter extends AbstractCSVFormatter implements ICSVFormatter{

    @Override
    protected void onInitialize() {
        this.setKey(Constants.Keys.EVENTS);
        this.setDelimiter('|');
        this.setNullString("\\N");
        this.setExtractEvents(Arrays.asList("solutionCanceled","active","solution_executed","solution_executing","deliver"));
        this.setExtractColumns(Arrays.asList("_id","imsi1","model","linux_v","tag","pkgn","brand","appid","did","sv",
                "imei1","apil","time_stamp","aid","solution_id","executed_status","exit_code","nut_code","nut_exist",
                "isvm","islimit","ishistory","isknown","elapsed_realtime","solution_id","mem_total","system_free_space",
                "data_free_space","mem_free", "exit_id","core_log","status","times"
        ));
    }
}
