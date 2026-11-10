package ru.nsu.esapronov.task131;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Пример поиска подстроки. */
public class Main {
    public static void main(String[] args) throws IOException {
        Path file = Files.createTempFile("task131-", ".txt");
        try {
            Files.writeString(file, "абракадабра", StandardCharsets.UTF_8);
            System.out.println(SubstringSearch.find(file.toString(), "бра"));
        } finally {
            Files.deleteIfExists(file);
        }
    }
}

