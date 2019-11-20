package org.whileinside.json.database;

import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class Container<T> {

    private Path path;
    private Database database;
    private Class<T> cls;

    @Setter
    private Function<T, String> stringTransform;

    @Setter
    private Function<T, Path> pathTransform;

    @Setter
    private ObjectInitializer<T> initializer;

    Container(Database database, String name, Class<T> cls) {
        this.path = database.getPath().resolve(name);
        this.database = database;
        this.cls  = cls;
        this.stringTransform = T::toString;
        this.pathTransform = t -> resolve(stringTransform.apply(t));
        this.initializer = ObjectInitializer.empty();

        createDirectory();
    }

    public void purgeDirectory() {
        try {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

            createDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDirectory() {
        if(!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(T object) {
        database.getWrapper().toJson(object, getPath(object));
    }

    public Path resolve(String name) {
        return path.resolve(name + ".json");
    }

    public Path resolve(T object) {
        return resolve(stringTransform.apply(object));
    }

    private Path getPath(T object) {
        Path path = pathTransform.apply(object);

        if(path == null) {
            path = resolve(stringTransform.apply(object));
        }

        return path;
    }

    public boolean exists(T object) {
        return Files.exists(getPath(object));
    }

    public boolean exists(String name) {
        return Files.exists(resolve(name));
    }

    public List<T> readAll() {
        List<T> objects = new ArrayList<>();

        try {
            Files.list(path)
                    .filter(file -> file.getFileName().toString().endsWith(".json"))
                    .forEach(file -> objects.add(database.getWrapper().fromJson(file, cls)));
        } catch (IOException e) {
            System.out.println("WARN: Cannot read file");
        }

        // инициализируем объекты
        objects.forEach(object -> initializer.init(object));

        return objects;
    }

    public void remove(T object) {
        try {
            Files.deleteIfExists(getPath(object));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(String name) {
        try {
            Files.deleteIfExists(resolve(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public T update(T object) {
        T updatedObject = database.getWrapper().fromJson(getPath(object), cls);

        initializer.init(updatedObject);

        return updatedObject;
    }

    public T read(String name) {
        T object = database.getWrapper().fromJson(resolve(name), cls);

        initializer.init(object);

        return object;
    }

}
