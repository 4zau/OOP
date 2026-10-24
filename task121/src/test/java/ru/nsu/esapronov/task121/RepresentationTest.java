package ru.nsu.esapronov.task121;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RepresentationTest {
    @Test
    void factoryCreatesRequestedImplementations() {
        assertInstanceOf(AdjacencyListGraph.class, Representation.ADJACENCY_LIST.create());
        assertInstanceOf(AdjacencyMatrixGraph.class, Representation.ADJACENCY_MATRIX.create());
        assertInstanceOf(IncidenceMatrixGraph.class, Representation.INCIDENCE_MATRIX.create());
    }

    @Test
    void factoryCreatesIndependentGraphsOfAnyVertexType() {
        for (Representation representation : Representation.values()) {
            Graph<Integer> first = representation.create();
            Graph<Integer> second = representation.create();
            assertNotSame(first, second);
            assertTrue(first.addVertex(42));
            assertFalse(second.containsVertex(42));
        }
    }
}
