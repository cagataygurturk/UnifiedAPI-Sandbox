package com.home24.api;

import java.io.File;
import java.util.HashMap;

/**
 * This is intended to emulate run-time & loading for Gon library and do unserialization of provided json string
 * Example simulated is as follows:
 *
 * Gson gson = new GsonBuilder().create();
 * Response r = gson.fromJson(jsonString, Response.class)
 *
 * If all goes well, there are no exceptions occurred meaning that result holds unserialized json object
 */
public class Main
{
    public static void main(String[] arguments) throws Exception
    {
        try {
            //"DI":
            String pathToLoad  = arguments[0];
            String jsonString  = arguments[1];//yes, index out of bounds with no check
            final File jarFile = new File(pathToLoad);

            HashMap<String, File> loaderMap = new HashMap<String, File>() {
                {
                    put("com.google.gson.*", jarFile);
                }
            };

            Loader classLoader = new Loader(loaderMap);
            Wrapper wrapper    = new Wrapper(classLoader);

            //try to load stuff:
            Object gson = wrapper.callMethod("com.google.gson.GsonBuilder", "create");
            Object json = wrapper.callMethod(gson, "fromJson", new Argument(String.class, jsonString), new Argument(Class.class, Object.class));
            Object result = wrapper.callMethod(gson, "toJson", new Argument(Object.class, json));

            //do some "work":
            System.out.println("List of loaded jars:");
            for (File loadedJar : classLoader.getLoaded()) {
                System.out.println((String) loadedJar.getAbsolutePath());
            }
            System.out.println("Json loopback (must match input argument):");
            System.out.println((String) result);

        } catch (Exception anyException) {
            anyException.printStackTrace();
        }
    }
}
