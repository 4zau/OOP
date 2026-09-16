package ru.nsu.esapronov.task111;

import java.util.Arrays;

/**
 * Класс способный применять heapsort к массиву.
 */
public class HeapSort {
    /**
     * Сортирует массив пирамидальной сортировкой.
     *
     * @param array массив который будет отсортирован.
     */
    public static void sort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        int n = array.length;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            heapify(array, i, 0);
        }
    }

    /**
     * Преобразует массив в кучу.
     *
     * @param array сам массив.
     * @param n размер кучи.
     * @param i индекс корня.
     */
    private static void heapify(int[] array, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && array[left] > array[largest]) {
            largest = left;
        }

        if (right < n && array[right] > array[largest]) {
            largest = right;
        }

        if (largest != i) {
            int swap = array[i];
            array[i] = array[largest];
            array[largest] = swap;

            heapify(array, n, largest);
        }
    }

    public static void test(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }

        long startTime = System.nanoTime();
        HeapSort.sort(array);
        long endTime = System.nanoTime();

        System.out.println(endTime - startTime);

        System.out.println(size * (Math.log(size) / Math.log(2)));
    }

    public static void main(String[] args) {
        test(10_000);

        test(1_000_000);

        test(5_000_000);

        test(10_000_000);
    }
}
