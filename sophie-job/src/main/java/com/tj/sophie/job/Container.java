package com.tj.sophie.job;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tj.sophie.ReflectionUtil;
import com.tj.sophie.core.IActionService;
import com.tj.sophie.core.IHandler;
import com.tj.sophie.guice.Binding;
import com.tj.sophie.guice.Handler;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by mbp on 6/4/15.
 */
public class Container {

    public static final String JARS = "sophie-jars";
    public static final String JOB_NAME = "sophie-configuration-name";

    private static Container ourInstance = new Container();

    public static Container getInstance() {
        return ourInstance;
    }

    private Injector injector;

    private boolean initialized = false;
    private Configuration configuration = null;
    private Path jarPath;


    private Container() {
    }


    public IActionService getActionService() {
        return this.injector.getInstance(IActionService.class);
    }

    public synchronized void initialize(Configuration configuration) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        if (this.initialized) {
            return;
        }
        this.initialized = true;
        this.configuration = configuration;
        this.jarPath = new Path(configuration.get(JARS));
        this.initialize();
    }


    public static Path uploadToHDFS(Configuration configuration, String localPath) throws IOException {
        File file = new File(localPath);
        String local = file.getName();
        FileSystem fs = FileSystem.get(configuration);
        Path to = new Path(getJobCachePath(configuration), local);
        fs.copyFromLocalFile(new Path(localPath), to);
        return to;
    }

    private Path downloadToLocal(Path path) throws IOException {
        FileSystem fs = FileSystem.get(this.configuration);
        Path to = new Path("/tmp/hadoop-cache/", path.getName());
        fs.copyToLocalFile(path, to);
        return to;
    }

    public static Path getJobCachePath(Configuration configuration) {
        Path root = new Path("/job_cache/", configuration.get(JOB_NAME) == null || configuration.get(JOB_NAME).trim().isEmpty() ? "default" : configuration.get(JOB_NAME).trim());
        return root;
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

        Path local = this.downloadToLocal(this.jarPath);

        List<Class<?>> result = new ArrayList<>();
        String jarFilePath = local.toUri().getPath();

        List<JarEntry> entries = this.filter(new JarFile(jarFilePath));
        for (JarEntry entry : entries) {
            String name = entry.getName();
            name = name.replace('/', '.');
            name = name.replace(".class", "");
            Class<?> clazz = this.getClass().getClassLoader().loadClass(name);
            result.add(clazz);
        }

        FileUtils.forceDelete(new File(jarFilePath));

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
}
