package com.tj.sophie.job;

import com.google.inject.Injector;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.guice.Handler;

import java.util.List;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mbp on 6/2/15.
 */
public class Container {
    private static Container ourInstance = new Container();

    public static Container getInstance() {
        return ourInstance;
    }

    private Injector injector;

    private Container() {
        JarFile jarFile = this.getJarPath();

    }

    public IActionService getActionService() {
        return this.injector.getInstance(IActionService.class);
    }


    private JarFile getJarPath() {
        Pattern pattern = Pattern.compile("^file:(?<jar>.*?)!");
        JarFile jar = null;
        try {
            String text = Container.class.getResource("").getFile();
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String jarPath = matcher.group("jar");
                if (jarPath != null && !jarPath.trim().isEmpty()) {
                    jar = new JarFile(jarPath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jar;
    }
}
