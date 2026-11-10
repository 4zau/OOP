package ru.nsu.esapronov.task131;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Проверки, запускаемые с помощью обычного JDK. */
public class SubstringSearchTest {
    public static void main(String[] args) throws IOException {
        Path file = Files.createTempFile("task131-", ".txt");
        try {
            check(file, "абракадабра", "бра", List.of(1L, 8L));
            check(file, "ааа", "аа", List.of(0L, 1L));
            check(file, "漢😀б漢😀б", "😀б", List.of(1L, 4L));
            check(file, "", "бра", List.of());
            check(file, "а", "аб", List.of());
            checkLargeFile(file);
        } finally {
            Files.deleteIfExists(file);
        }
        System.out.println("all tests passed");
    }

    private static void check(Path file, String text, String pattern, List<Long> expected)
            throws IOException {
        Files.writeString(file, text, StandardCharsets.UTF_8);
        assertPositions(expected, SubstringSearch.find(file.toString(), pattern));
    }

    private static void checkLargeFile(Path file) throws IOException {
        char[] block = new char[8192];
        Arrays.fill(block, 'я');
        int repetitions = 4096;

        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (int i = 0; i < repetitions; i++) {
                writer.write(block);
            }

            writer.write(block, 0, block.length - 1);
            writer.write("😀бра");
        }

        long expected = (long) repetitions * block.length + block.length - 1;
        List<Long> positions = new ArrayList<>();
        SubstringSearch.find(file.toString(), "😀бра", positions::add);
        assertPositions(List.of(expected), positions);
    }

    private static void assertPositions(List<Long> expected, List<Long> actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("expected " + expected + ", received " + actual);
        }
    }
}
