package com.home24.api;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class Loader
{
    private HashMap<String, File> loadedJars = new HashMap<String, File>();

    private HashMap<String, File> loaderMap;

    public Loader(HashMap<String, File> loaderMap)
    {
        this.loaderMap = loaderMap;
    }

    /**
     * Load jar by class name. It will try to resolve jar file path via mapping specified on creation
     * If nothing matched: do nothing (weird?)
     */
    public void load(String className)
    {
        File jarFile = this.matchJar(className);
        if (jarFile != null) {
            this.load(jarFile);
        }
    }

    /**
     * Load jar directly by system path. Does not handle exceptions, only shows them somehow
     */
    public void load(File jarFile)
    {
        if (this.loadedJars.get(jarFile.getAbsolutePath()) == null) {
            try {
                this.loadJar(jarFile);
                this.loadedJars.put(jarFile.getAbsolutePath(), jarFile);
            } catch (Exception anyException) {
                anyException.printStackTrace();//
            }
        }
    }

    public File[] getLoaded()
    {
        File[] files = new File[this.loadedJars.size()];
        return this.loadedJars.values().toArray(files);
    }

    private void loadJar(File jarFile) throws Exception
    {
        URL url = jarFile.toURI().toURL();
        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        method.setAccessible(true);
        method.invoke(classLoader, url);
    }

    //yet another hash-map "iteration" : I really hope there is more strict way with involving types
    //otherwise: why do I specify types in the map if I need to explicitly cast keys/values?
    private File matchJar(String className)
    {
        for (Object hash : this.loaderMap.entrySet()) {
            Map.Entry entry = (Map.Entry) hash;
            String key = (String) entry.getKey();
            if (className.matches(key)) {
                return (File) entry.getValue();
            }
        }

        return null;
    }
}
