package ru.nsu.esapronov.task121;

import java.io.IOException;
import java.nio.file.Path;

public final class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("please input filename");
            System.exit(1);
        }
        try {
            Graph<Integer> graph = Graph.readFromFile(
                    Path.of(args[0]), Representation.ADJACENCY_LIST, Integer::parseInt);
            System.out.println(graph);
            System.out.println("Kahn: " + new KahnTopologicalSort().sort(graph));
            System.out.println("DFS:  " + new DfsTopologicalSort().sort(graph));
        } catch (IOException | IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        }
    }
}
