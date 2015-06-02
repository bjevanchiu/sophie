package com.tj.report.sophie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mbp on 6/2/15.
 */
public class VerbService implements IVerbService {

    private Map<VerbId, List<IHandler>> handlers = new HashMap<VerbId, List<IHandler>>();

    public void register(IHandler handler) {
        if (this.handlers.containsKey(handler.getVerbId())) {
            List<IHandler> list = this.handlers.get(handler.getVerbId());
            if (!list.contains(handler)) {
                list.add(handler);
            }
        } else {
            ArrayList<IHandler> list = new ArrayList<IHandler>();
            list.add(handler);
            this.handlers.put(handler.getVerbId(), list);
        }

    }

    public void unregister(IHandler handler) {

    }

    public void execute(VerbId verbId, Context context) {
        List<IHandler> list = new ArrayList<IHandler>();
        if(this.handlers.containsKey(verbId))
        {
            list = this.handlers.get(verbId);
        }
        // TODO 需要对list中的handler根据优先级排序
        for(IHandler handler : list)
        {
            handler.execute(context);
        }
    }
}
