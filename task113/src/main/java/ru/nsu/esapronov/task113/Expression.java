package ru.nsu.esapronov.task113;

import java.util.HashMap;
import java.util.Map;

/**
 * Математическое выражение, которое можно вычислить и дифференцировать.
 */
abstract class Expression {
    /**
     * Строит новое выражение для производной по указанной переменной.
     *
     * @param variable имя переменной дифференцирования
     * @return производная в виде нового дерева выражения
     */
    public abstract Expression derivative(String variable);

    /**
     * Вычисляет выражение по уже разобранным значениям переменных.
     *
     * @param variables соответствие имён переменных их значениям
     * @return значение выражения
     */
    protected abstract int eval(Map<String, Integer> variables);

    /**
     * Вычисляет выражение по строке вида x = 10; y = 13.
     *
     * @param assignments значения переменных, разделённые точкой с запятой
     * @return значение выражения
     * @throws IllegalArgumentException если строка неверна или нет значения нужной переменной
     * @throws ArithmeticException при делении на ноль
     */
    public final int eval(String assignments) {
        Map<String, Integer> variables = new HashMap<>();
        if (!assignments.isBlank()) {
            for (String assignment : assignments.split(";", -1)) {
                String[] pair = assignment.split("=", -1);
                if (pair.length != 2 || !Variable.validName(pair[0].strip())) {
                    throw new IllegalArgumentException("invalid assignment: " + assignment);
                }

                String name = pair[0].strip();
                int value;
                try {
                    value = Integer.parseInt(pair[1].strip());
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("invalid value: " + assignment, ex);
                }
                if (variables.putIfAbsent(name, value) != null) {
                    throw new IllegalArgumentException("duplicate variable: " + name);
                }
            }
        }
        return eval(variables);
    }

    /** Выводит выражение в стандартный поток вывода. */
    public final void print() {
        System.out.println(this);
    }

    /**
     * Разбирает строку, в которой каждая бинарная операция заключена в скобки.
     * Например: (3+(2*x)).
     *
     * @param text запись выражения
     * @return построенное дерево выражения
     * @throws IllegalArgumentException если запись некорректна
     */
    public static Expression parse(String text) {
        return new Parser(text).parse();
    }
}