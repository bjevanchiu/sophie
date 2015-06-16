package com.tj.sophie.job.service;

import com.google.gson.JsonObject;
import com.tj.sophie.core.AbstractService;
import com.tj.sophie.guice.Binding;
import com.tj.sophie.job.helper.Helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mbp on 6/10/15.
 */
@Binding(from = IFilterService.class, to = FilterService.class)
public class FilterService extends AbstractService implements IFilterService {

    private Pattern pattern = Pattern.compile("^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3}) (?<ssid>\\d+) *(?<did>[0-9a-fA-F-]+)? *(?<level>\\w+) *\\[(?<class>.*?)\\] *\\(.*?\\) *(?<content>[^\\{]+?)(?<json>\\{.*\\})?$");

    @Override
    protected void onInitialize() {

    }

    @Override
    public boolean accept(String input) {
        Matcher matcher = pattern.matcher(input);
        String content = null;
        String clazz = null;
        while (matcher.find()) {
            clazz = matcher.group("class");
            content = matcher.group("content");
        }
        if (!Helper.isNullOrEmpty(clazz)) {
            if (clazz.equalsIgnoreCase("com.tj.arms.sword.SwordServlet")
                    || clazz.equalsIgnoreCase("com.tj.arms.sword.solution.SolutionService")
                    || clazz.equalsIgnoreCase("com.tj.arms.sword.redis.JedisContext")

                    || clazz.equalsIgnoreCase("com.tj.arms.sword.user.UserService")
                    || clazz.equalsIgnoreCase("com.tj.arms.sword.user.UserServiceImpl")

                    || clazz.equalsIgnoreCase("com.tj.arms.sword.user.UserRepository")
                    || clazz.equalsIgnoreCase("com.tj.arms.sword.user.UserRepositoryImpl")

                    || clazz.equalsIgnoreCase("com.tj.arms.sword.DefaultEngine")

                    || clazz.equalsIgnoreCase("com.tj.arms.sword.switches.SwitchPropertiesService")
                    || clazz.equalsIgnoreCase("com.tj.arms.sword.switches.SwitchPropertiesServiceImpl")

                    || clazz.equalsIgnoreCase("com.tj.arms.sword.request.RequestHistoryRepository")
                    || clazz.equalsIgnoreCase("com.tj.arms.sword.request.RequestHistoryRepositoryImpl")

                    || clazz.equalsIgnoreCase("com.tj.arms.sword.solution.SolutionComponent")
                    || clazz.equalsIgnoreCase("com.tj.arms.sword.root.KnownRootComponent")
                    || clazz.equalsIgnoreCase("com.tj.arms.sword.defence.DefendServiceImpl")) {
                return true;
            }
        }
        if (!Helper.isNullOrEmpty(content)) {
            content = content.trim();
            if (content.startsWith("收到日志")
                    || content.startsWith("种子日志")
                    || content.startsWith("虚拟机检测")
                    || content.startsWith("是否易打工")
                    || content.startsWith("x-forwarded-for")
                    || content.startsWith("x-real-ip")
                    || content.startsWith("user-agent")
                    || content.startsWith("ms:")
                    || content.startsWith("in:")
                    || content.startsWith("out:")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean acceptEvent(String event, JsonObject jsonObject) {
        if (jsonObject.has("eventId")) {
            return Helper.equalsIgnoreCase(event, jsonObject.get("eventId").getAsString());
        }
        return false;
    }
}
