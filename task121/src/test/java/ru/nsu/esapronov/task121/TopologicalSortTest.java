package ru.nsu.esapronov.task121;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

abstract class TopologicalSortTest {
    protected abstract TopologicalSort createSorter();

    private void assertValidOrder(Graph<Integer> graph, List<Integer> order) {
        assertEquals(graph.vertices().size(), order.size());
        assertEquals(new HashSet<>(graph.vertices()), new HashSet<>(order));
        Map<Integer, Integer> position = new HashMap<>();
        for (int i = 0; i < order.size(); i++) {
            position.put(order.get(i), i);
        }
        for (Edge<Integer> edge : graph.edges()) {
            assertTrue(position.get(edge.from()) < position.get(edge.to()),
                    () -> "Incorrect order for " + edge);
        }
    }

    @ParameterizedTest
    @EnumSource(Representation.class)
    void sortsEmptyAndIsolatedVertices(Representation representation) {
        Graph<Integer> graph = representation.create();
        TopologicalSort sorter = createSorter();
        assertEquals(List.of(), sorter.sort(graph));
        graph.addVertex(1);
        assertEquals(List.of(1), sorter.sort(graph));
        graph.addVertex(2);
        graph.addVertex(3);
        assertValidOrder(graph, sorter.sort(graph));
    }

    @ParameterizedTest
    @EnumSource(Representation.class)
    void sortsChainRegardlessOfInsertionOrder(Representation representation) {
        Graph<Integer> graph = representation.create();
        for (int vertex : List.of(4, 3, 2, 1)) {
            graph.addVertex(vertex);
        }
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        assertEquals(List.of(1, 2, 3, 4), createSorter().sort(graph));
    }

    @ParameterizedTest
    @EnumSource(Representation.class)
    void sortsDiamondAndDisconnectedComponentWithoutChangingGraph(
            Representation representation) {
        Graph<Integer> graph = representation.create();
        for (int vertex = 1; vertex <= 7; vertex++) {
            graph.addVertex(vertex);
        }
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);
        graph.addEdge(5, 6);
        List<Integer> before = graph.vertices();
        final Set<Edge<Integer>> edgesBefore = graph.edges();
        TopologicalSort sorter = createSorter();
        List<Integer> order = sorter.sort(graph);
        assertValidOrder(graph, order);
        assertThrows(UnsupportedOperationException.class, () -> order.add(8));
        assertEquals(before, graph.vertices());
        assertEquals(edgesBefore, graph.edges());
        assertValidOrder(graph, sorter.sort(graph));
    }

    @ParameterizedTest
    @EnumSource(Representation.class)
    void rejectsCycleAndCanBeReusedAfterFailure(Representation representation) {
        Graph<Integer> graph = representation.create();
        for (int vertex = 0; vertex < 4; vertex++) {
            graph.addVertex(vertex);
        }
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);
        TopologicalSort sorter = createSorter();
        Set<Edge<Integer>> before = graph.edges();
        assertThrows(IllegalArgumentException.class, () -> sorter.sort(graph));
        assertEquals(before, graph.edges());
        graph.removeEdge(3, 1);
        assertValidOrder(graph, sorter.sort(graph));
    }

    @ParameterizedTest
    @EnumSource(Representation.class)
    void rejectsSelfLoopInSeparateComponent(Representation representation) {
        Graph<Integer> graph = representation.create();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addEdge(1, 2);
        graph.addEdge(3, 3);
        assertThrows(IllegalArgumentException.class, () -> createSorter().sort(graph));
    }

    @Test
    void sortsRandomAcyclicGraphs() {
        Random random = new Random(965686);
        for (Representation representation : Representation.values()) {
            Graph<Integer> graph = representation.create();
            for (int vertex = 24; vertex >= 0; vertex--) {
                graph.addVertex(vertex);
            }

            for (int from = 0; from < 25; from++) {
                for (int to = from + 1; to < 25; to++) {
                    if (random.nextInt(4) == 0) {
                        graph.addEdge(from, to);
                    }
                }
            }
            assertValidOrder(graph, createSorter().sort(graph));
        }
    }

    @Test
    void rejectsNullAndSupportsDifferentVertexTypes() {
        TopologicalSort sorter = createSorter();
        assertThrows(NullPointerException.class, () -> sorter.sort(null));
        Graph<String> strings = new AdjacencyListGraph<>();
        strings.addVertex("second");
        strings.addVertex("first");
        strings.addEdge("first", "second");
        assertEquals(List.of("first", "second"), sorter.sort(strings));
        Graph<Integer> integers = new AdjacencyListGraph<>();
        integers.addVertex(1);
        assertEquals(List.of(1), sorter.sort(integers));
    }
}
