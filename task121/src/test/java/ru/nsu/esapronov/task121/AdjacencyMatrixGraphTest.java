package ru.nsu.esapronov.task121;

class AdjacencyMatrixGraphTest extends GraphTest {
    @Override
    protected Graph<String> createGraph() {
        return new AdjacencyMatrixGraph<>();
    }
}
