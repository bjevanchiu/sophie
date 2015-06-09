package com.tj.sophie.job.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by mbp on 6/9/15.
 */
public final class JarFileHelper {

    private List<Class<?>> types = new ArrayList<>();

    private JarFileHelper(String path) throws IOException, ClassNotFoundException {
        this.types = this.loadTypes(path);
    }

    public static JarFileHelper create(String path) throws IOException, ClassNotFoundException {
        return new JarFileHelper(path);
    }

    public List<Class<?>> getTypes() {
        return this.types;
    }

    private List<Class<?>> loadTypes(String path) throws IOException, ClassNotFoundException {
        JarFile jarFile = new JarFile(path);
        List<JarEntry> jarEntries = new ArrayList<>();
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
            jarEntries.add(jarEntry);
        }
        List<Class<?>> types = new ArrayList<>();
        for (JarEntry entry : jarEntries) {
            String name = entry.getName();
            name = name.replace('/', '.');
            name = name.replace(".class", "");
            Class<?> clazz = this.getClass().getClassLoader().loadClass(name);
            types.add(clazz);
        }
        return types;
    }
}
