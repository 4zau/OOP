package ru.nsu.esapronov.task121;

class KahnTopologicalSortTest extends TopologicalSortTest {
    @Override
    protected TopologicalSort createSorter() {
        return new KahnTopologicalSort();
    }
}
