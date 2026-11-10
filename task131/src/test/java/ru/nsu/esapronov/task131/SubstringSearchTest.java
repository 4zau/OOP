package ru.nsu.esapronov.task131;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;

/** Тесты поиска подстроки в UTF-8-файлах. */
public class SubstringSearchTest {
    @TempDir(cleanup = CleanupMode.ALWAYS)
    Path directory;

    /** Проверяет вхождения. */
    @Test
    void findsMatchesInSmallFiles() throws IOException {
        Path file = directory.resolve("input.txt");
        check(file, "абракадабра", "бра", List.of(1L, 8L));
        check(file, "ааа", "аа", List.of(0L, 1L));
        check(file, "漢😀б漢😀б", "😀б", List.of(1L, 4L));
        check(file, "😀абра", "бра", List.of(2L));
        check(file, "😀😀😀", "😀😀", List.of(0L, 1L));
        check(file, "abababac", "ababac", List.of(2L));
        check(file, "aaaab", "aaab", List.of(1L));
        check(file, "abc", "xyz", List.of());
        check(file, "", "бра", List.of());
        check(file, "а", "аб", List.of());
    }

    /** Проверяет поиск в файле, который больше доступной памяти. */
    @Test
    void findsMatchInLargeFile() throws IOException {
        checkLargeFile(directory.resolve("large.txt"));
    }

    /** Пустой шаблон не допускается. */
    @Test
    void rejectsEmptyPattern() throws IOException {
        Path file = directory.resolve("empty.txt");
        Files.writeString(file, "text", StandardCharsets.UTF_8);
        assertThrows(IllegalArgumentException.class,
                () -> SubstringSearch.find(file.toString(), ""));
    }

    /** Ошибка открытия файла передаётся вызывающему коду. */
    @Test
    void rejectsMissingFile() {
        assertThrows(IOException.class,
                () -> SubstringSearch.find(directory.resolve("missing.txt").toString(), "a"));
    }

    private static void check(Path file, String text, String pattern, List<Long> expected)
            throws IOException {
        Files.writeString(file, text, StandardCharsets.UTF_8);
        assertPositions(expected, SubstringSearch.find(file.toString(), pattern));
    }

    private static void checkLargeFile(Path file) throws IOException {
        char[] block = new char[8192];
        Arrays.fill(block, 'a');
        long fileSize = 10_000_000_000L;
        String pattern = "😀бра";
        long expected = fileSize - pattern.getBytes(StandardCharsets.UTF_8).length;

        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (long i = 0; i < expected / block.length; i++) {
                writer.write(block);
            }

            writer.write(block, 0, (int) (expected % block.length));
            writer.write(pattern);
        }

        assertEquals(fileSize, Files.size(file));
        assertTrue(fileSize > Runtime.getRuntime().maxMemory());
        List<Long> positions = new ArrayList<>();
        SubstringSearch.find(file.toString(), pattern, positions::add);
        assertPositions(List.of(expected), positions);
    }

    private static void assertPositions(List<Long> expected, List<Long> actual) {
        assertEquals(expected, actual);
    }
}
