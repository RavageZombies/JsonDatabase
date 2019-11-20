package org.whileinside.json.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.nio.file.Path;

public class Test {

    public static void main(String[] args) {
        Database database = Database.newDatabase("local");

        Container<User> container = database.newContainer("users", User.class);
        container.setStringTransform(User::getName);
        container.setPathTransform(User::getPath);
        container.setInitializer(container::resolve);

        container.readAll().forEach(System.out::println);
    }

    @RequiredArgsConstructor
    @Getter
    @ToString
    private static class User {
        private final String name;
        private final int balance;
        private final int kills;
        private final int deaths;

        private transient Path path;
    }

}
