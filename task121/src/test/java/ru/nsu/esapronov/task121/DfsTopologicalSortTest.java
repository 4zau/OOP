package ru.nsu.esapronov.task121;

class DfsTopologicalSortTest extends TopologicalSortTest {
    @Override
    protected TopologicalSort createSorter() {
        return new DfsTopologicalSort();
    }
}
