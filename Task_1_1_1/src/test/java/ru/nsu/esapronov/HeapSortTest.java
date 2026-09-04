package ru.nsu.esapronov;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeapSortTest {

    @Test
    void sort() {
        int[] array = new int[]{3,1,2};
        HeapSort.sort(array);
        assertArrayEquals(new int[]{1,2,3}, array);
    }
}