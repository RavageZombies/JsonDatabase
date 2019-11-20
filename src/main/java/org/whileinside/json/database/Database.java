package org.whileinside.json.database;

import lombok.Getter;
import lombok.Setter;
import org.whileinside.json.database.wrapper.JsonWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

@Getter
public class Database {

    private Path path;

    @Setter
    private JsonWrapper wrapper;

    private Database(String name) {
        this.path = Paths.get(name);
        this.wrapper = JsonWrapper.get();

        createDirectory();
    }

    public static Database newDatabase(String name) {
        return new Database(name);
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

    public <T> Container<T> newContainer(String name, Class<T> cls) {
        return new Container<>(this, name, cls);
    }

}
