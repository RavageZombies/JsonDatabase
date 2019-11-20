package org.whileinside.json.database.wrapper;

import com.google.gson.Gson;
import lombok.Getter;
import org.whileinside.json.database.exception.JsonException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class GsonWrapper extends JsonWrapper {

    @Getter
    protected Gson handle = new Gson();

    @Override
    public void toJson(Object object, Path path) {
        try(OutputStream os = Files.newOutputStream(path); OutputStreamWriter osw = new OutputStreamWriter(os)) {
            getHandle().toJson(object, osw);
        } catch (IOException e) {
            throw new JsonException("Cannot write to file " + path, e);
        }
    }

    @Override
    public <T> T fromJson(Path path, Class<T> cls) {
        try(InputStream is = Files.newInputStream(path); InputStreamReader isr = new InputStreamReader(is)) {
            return getHandle().fromJson(isr, cls);
        } catch (IOException e) {
            throw new JsonException("Cannot read from file " + path, e);
        }
    }
}
