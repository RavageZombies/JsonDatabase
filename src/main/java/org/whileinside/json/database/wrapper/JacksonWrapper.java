package org.whileinside.json.database.wrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.whileinside.json.database.exception.JsonException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class JacksonWrapper extends JsonWrapper {

    @Getter
    private ObjectMapper handle = new ObjectMapper();

    @Override
    public void toJson(Object object, Path path) {
        try(OutputStream os = Files.newOutputStream(path); OutputStreamWriter osw = new OutputStreamWriter(os)) {
            handle.writeValue(osw, object);
        } catch (IOException e) {
            throw new JsonException("Cannot write to file " + path, e);
        }
    }

    @Override
    public <T> T fromJson(Path path, Class<T> cls) {
        try(InputStream is = Files.newInputStream(path); InputStreamReader isr = new InputStreamReader(is)) {
            return handle.readValue(isr, cls);
        } catch (IOException e) {
            throw new JsonException("Cannot read from file " + path, e);
        }
    }


}
