package ru.nsu.esapronov.task122;

/** Демонстрация работы хеш-таблицы. */
public class Main {
    /**
     * Добавляет ключ, обновляет значение и выводит результат.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        System.out.println(hashTable.get("one"));
    }
}
