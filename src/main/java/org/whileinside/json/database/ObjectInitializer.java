package org.whileinside.json.database;

public interface ObjectInitializer<T> {

    static <T> ObjectInitializer<T> empty() {
        return object -> {};
    }

    void init(T object);

}
