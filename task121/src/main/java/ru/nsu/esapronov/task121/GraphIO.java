package ru.nsu.esapronov.task121;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Формат: N M, затем N строк с именами вершин, затем M строк "from to".
 */
public final class GraphIO {
    public static Graph<String> read(Path path, Representation representation)
            throws IOException {
        Graph<String> graph = representation.create();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String[] header = fields(reader, 1, 2);
            int n = count(header[0]);
            int m = count(header[1]);

            for (int i = 0; i < n; i++) {
                String name = fields(reader,  i + 2, 1)[0];
                if (!graph.addVertex(name)) {
                    throw new IOException("Duplicate vertex: " + name);
                }
            }

            for (int i = 0; i < m; i++) {
                String[] edge = fields(reader, n + i + 2, 2);
                if (!graph.containsVertex(edge[0]) || !graph.containsVertex(edge[1])) {
                    throw new IOException("Unknown endpoint: " + String.join(" ", edge));
                }
                if (!graph.addEdge(edge[0], edge[1])) {
                    throw new IOException("Duplicate edge: " + String.join(" ", edge));
                }
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    throw new IOException("Unexpected data after the last edge");
                }
            }
        }
        return graph;
    }

    private static int count(String value) throws IOException {
        try {
            int result = Integer.parseInt(value);
            if (result < 0) {
                throw new NumberFormatException();
            }
            return result;
        } catch (NumberFormatException exception) {
            throw new IOException("Expected a positive integer: " + value, exception);
        }
    }

    private static String[] fields(BufferedReader reader, int lineNumber, int expected)
            throws IOException {
        String line = reader.readLine();

        if (line == null || line.isBlank()) {
            throw new IOException("Missing data on line " + lineNumber);
        }

        String[] result = line.strip().split("\\s+");
        if (result.length != expected) {
            throw new IOException("Expected " + expected + " fields on line " + lineNumber);
        }
        return result;
    }
}
