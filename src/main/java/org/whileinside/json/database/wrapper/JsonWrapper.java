package org.whileinside.json.database.wrapper;

import java.nio.file.Path;

public abstract class JsonWrapper {

    private static JsonWrapper instance;

    public static JsonWrapper get() {
        if(instance == null) {
            instance = detect();
        }

        return instance;
    }

    private static JsonWrapper detect() {
        try {
            Class.forName("com.google.gson.Gson");

            return new GsonWrapper();
        } catch (ClassNotFoundException ignored) { }

        try {
            Class.forName("com.fasterxml.jackson.databind.ObjectMapper");

            return new JacksonWrapper();
        } catch (ClassNotFoundException ignored) { }

        throw new IllegalStateException("Cannot find Json Library. Install Gson or Jackson");
    }

    public abstract Object getHandle();

    public abstract void toJson(Object object, Path path);

    public abstract <T> T fromJson(Path path, Class<T> cls);

}
