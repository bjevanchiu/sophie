package com.tj.sophie.job.model;

import java.util.*;

/**
 * Created by mbp on 6/4/15.
 */
public class SeedEventHistoryCSVFormatter extends AbstractCSVFormatter implements ICSVFormatter{

    @Override
    protected void onInitialize() {
        this.setKey("seed_event_history_csv");
        this.setDelimiter('|');
        this.setNullString("\\N");
        this.setExtractEvents(Arrays.asList("solutionCanceled,active,solution_executed,solution_executing,deliver"));
        this.setExtractColumns(Arrays.asList("_id,imsi1,model,linux_v,tag,pkgn,brand,appid,did,sv,imei1,apil,time_stamp,aid,solution_id,executed_status,exit_code,nut_code,nut_exist,isvm,islimit,ishistory,isknown,elapsed_realtime,id(solution_id),mem_total,system_free_space,data_free_space,mem_free,exit_id,core_log,status,times"));
    }
}
