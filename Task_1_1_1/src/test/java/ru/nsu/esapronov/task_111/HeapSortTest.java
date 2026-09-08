package ru.nsu.esapronov.task_1_1_1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HeapSortTest {
    @Test
    void testEmptyArray() {
        int[] array = {};
        assertDoesNotThrow(() -> HeapSort.sort(array));
        assertArrayEquals(new int[]{}, array);
    }

    @Test
    void testSingleElementArray() {
        int[] array = {42};
        HeapSort.sort(array);
        assertArrayEquals(new int[]{42}, array);
    }

    @Test
    void testAlreadySortedArray() {
        int[] array = {-5, 0, 1, 2, 8, 15};
        HeapSort.sort(array);
        assertArrayEquals(new int[]{-5, 0, 1, 2, 8, 15}, array);
    }

    @Test
    void testArray() {
        int[] array = {5, -12332, 54, 0, 4};
        HeapSort.sort(array);
        assertArrayEquals(new int[]{-12332, 0, 4, 5, 54}, array);
    }

    @Test
    void testArrayWithDuplicates() {
        int[] array = {3, 1, 2, 3, 1, 3};
        HeapSort.sort(array);
        assertArrayEquals(new int[]{1, 1, 2, 3, 3, 3}, array);
    }
}