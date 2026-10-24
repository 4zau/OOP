package ru.nsu.esapronov.task121;

class AdjacencyListGraphTest extends GraphTest {
    @Override
    protected Graph<String> createGraph() {
        return new AdjacencyListGraph<>();
    }
}
