package com.tj.sophie.job;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tj.sophie.ReflectionUtil;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IHandler;
import com.tj.sophie.guice.Binding;
import com.tj.sophie.guice.Handler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mbp on 6/4/15.
 */
public class Container {
    private static Container ourInstance = new Container();

    public static Container getInstance() {
        return ourInstance;
    }

    private Injector injector;


    private Container() {
        try {
            this.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    public IActionService getActionService() {
        return this.injector.getInstance(IActionService.class);
    }

    private void initialize() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {

        MainModule mainModule = new MainModule();

        List<Class<?>> classMetadatas = this.loadClassMetadatas();

        Map<Class<?>, Class<?>> mapper = new HashMap<>();
        for (Class<?> clazz : classMetadatas) {
            Binding binding = ReflectionUtil.findAnnotation(Binding.class, clazz);
            if (binding != null) {
                Class<?> from = binding.from();
                Class<?> to = binding.to();
                mapper.put(from, to);
            }
        }
        mainModule.initializeBinding(mapper);

        List<Class<IHandler>> handlerTypes = new ArrayList<>();
        for (Class<?> clazz : classMetadatas) {
            Handler handler = ReflectionUtil.findAnnotation(Handler.class, clazz);
            if (handler != null) {
                handlerTypes.add((Class<IHandler>) clazz);
            }
        }
        mainModule.initializeHandler(handlerTypes);

        this.injector = Guice.createInjector(mainModule);
        IActionService actionService = this.injector.getInstance(IActionService.class);
        for (Class<IHandler> type : handlerTypes) {
            IHandler handler = this.injector.getInstance(type);
            actionService.register(handler);
        }
    }


    private List<Class<?>> loadClassMetadatas() throws IOException, ClassNotFoundException {
        List<Class<?>> result = new ArrayList<>();
        String jarFilePath = this.getJarFilePath();
        Logger.debug("jar path %s", jarFilePath);

        File file = new File(jarFilePath);
        Collection<File> files = FileUtils.listFiles(file.getParentFile(), null, true);
        for (File x : files) {
            Logger.debug("load path %s", x.getAbsolutePath());
        }


        List<JarEntry> entries = this.filter(new JarFile(jarFilePath));
        for (JarEntry entry : entries) {
            String name = entry.getName();
            name = name.replace('/', '.');
            name = name.replace(".class", "");
            Class<?> clazz = this.getClass().getClassLoader().loadClass(name);
            result.add(clazz);
        }

        return result;
    }

    private List<JarEntry> filter(JarFile jarFile) {
        List<JarEntry> result = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            if (jarEntry.isDirectory()) {
                continue;
            }
            String name = jarEntry.getName();
            if (name.contains("$") || !name.endsWith(".class") || !name.startsWith("com/tj")) {
                continue;
            }
            result.add(jarEntry);
        }
        return result;

    }

    private String getJarFilePath() {
        String path = Container.class.getResource("").getFile();
        Logger.debug("resource path %s", path);
        //Pattern pattern = Pattern.compile("^.*?file:(?<jar>[^!]*)!");
        Pattern pattern = Pattern.compile("file:(?<path>.*)$");
        Matcher matcher = pattern.matcher(path);
        while (matcher.find()) {
            return matcher.group("path");
        }
        return null;
    }
}
