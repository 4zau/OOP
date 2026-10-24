package ru.nsu.esapronov.task121;

class IncidenceMatrixGraphTest extends GraphTest {
    @Override
    protected Graph<String> createGraph() {
        return new IncidenceMatrixGraph<>();
    }
}
