package ru.nsu.esapronov.task113;

import java.util.Map;

/** Переменная, в том числе с многобуквенным именем. */
final class Variable extends Expression {
    private final String name;

    /**
     * Создаёт переменную.
     *
     * @param name имя переменной
     * @throws IllegalArgumentException если имя некорректно
     */
    public Variable(String name) {
        if (!validName(name)) {
            throw new IllegalArgumentException("invalid variable name: " + name);
        }
        this.name = name;
    }

    /**
     * Проверяет имя переменной: буква или подчёркивание в начале,
     * далее также разрешены цифры.
     *
     * @param name проверяемое имя
     * @return true, если имя допустимо
     */
    static boolean validName(String name) {
        if (name == null || name.isEmpty()
                || !(Character.isLetter(name.charAt(0)) || name.charAt(0) == '_')) {
            return false;
        }
        for (int i = 1; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (!(Character.isLetterOrDigit(ch) || ch == '_')) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Expression derivative(String variable) {
        return new Number(name.equals(variable) ? 1 : 0);
    }

    @Override
    protected int eval(Map<String, Integer> variables) {
        Integer value = variables.get(name);
        if (value == null) {
            throw new IllegalArgumentException("no value for variable: " + name);
        }
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}
