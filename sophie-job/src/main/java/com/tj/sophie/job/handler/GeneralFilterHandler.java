package com.tj.sophie.job.handler;

import com.google.inject.Inject;
import com.tj.sophie.core.AbstractHandler;
import com.tj.sophie.core.IContext;
import com.tj.sophie.guice.Handler;
import com.tj.sophie.job.Constants;
import com.tj.sophie.job.service.Actions;
import org.slf4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mbp on 6/9/15.
 */
@Handler
public class GeneralFilterHandler extends AbstractHandler {
    private Pattern pattern = Pattern.compile("^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) (?<ssid>\\d+) *(?<level>\\w)+ *\\[(?<class>.*?)\\] *\\(.*?\\) *(?<content>[^\\{]+)$");

    @Inject
    private Logger logger;

    @Override
    protected void onInitialize() {
        this.setAction(Actions.GeneralFilter);
    }

    @Override
    protected void onExecute(IContext context) {
        String input = context.getInput();
        Matcher matcher = pattern.matcher(input);
        String content = null;
        while (matcher.find()) {
            content = matcher.group("content");
        }

        this.logger.info(String.format("content %s", content));

        if (content == null || content.trim().isEmpty()) {
            return;
        }
        content = content.trim();
        if (content.startsWith("收到日志")
                || content.startsWith("种子日志")
                || content.startsWith("x-forwarded-for")
                || content.startsWith("x-real-ip")
                || content.startsWith("user-agent")) {
            context.setVariable(Constants.FILTED_FLAG, Boolean.TRUE);
        }
    }
}
