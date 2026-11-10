package ru.nsu.esapronov.task131;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongConsumer;

/** Поиск подстроки в файле. */
public final class SubstringSearch {
    /** Возвращает индексы начиная с нуля. */
    public static List<Long> find(String filename, String substring) throws IOException {
        List<Long> positions = new ArrayList<>();
        find(filename, substring, positions::add);
        return positions;
    }

    /** Передаёт каждый найденный индекс сразу, без накопления результатов. */
    public static void find(String filename, String substring, LongConsumer onMatch)
            throws IOException {
        int[] pattern = substring.codePoints().toArray();
        if (pattern.length == 0) {
            throw new IllegalArgumentException("substring shouldn't be empty");
        }

        int[] prefix = prefixFunction(pattern);
        int matched = 0;
        long index = 0;

        try (BufferedReader reader =
                     Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8)) {
            int unit;
            while ((unit = reader.read()) != -1) {
                int codePoint = unit;
                if (Character.isHighSurrogate((char) unit)) {
                    // Декодер UTF-8 выдаёт кодовую точку вне BMP как пару char.
                    codePoint = Character.toCodePoint((char) unit, (char) reader.read());
                }

                while (matched > 0 && pattern[matched] != codePoint) {
                    matched = prefix[matched - 1];
                }
                if (pattern[matched] == codePoint) {
                    matched++;
                }

                if (matched == pattern.length) {
                    onMatch.accept(index - pattern.length + 1);
                    // Суффикс совпадения может начать следующее вхождение.
                    matched = prefix[matched - 1];
                }
                index++;
            }
        }
    }

    private static int[] prefixFunction(int[] pattern) {
        int[] prefix = new int[pattern.length];
        for (int i = 1; i < pattern.length; i++) {
            int matched = prefix[i - 1];

            while (matched > 0 && pattern[i] != pattern[matched]) {
                matched = prefix[matched - 1];
            }
            if (pattern[i] == pattern[matched]) {
                matched++;
            }
            prefix[i] = matched;
        }
        return prefix;
    }
}
