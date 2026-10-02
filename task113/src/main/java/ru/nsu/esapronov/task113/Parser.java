package ru.nsu.esapronov.task113;

import static java.util.Objects.requireNonNull;

/** Разбор записи выражения со скобками вокруг каждой операции. */
final class Parser {
    private final String text;
    private int position;

    /**
     * Создаёт разборщик для заданной строки.
     *
     * @param text строка с выражением
     */
    Parser(String text) {
        this.text = requireNonNull(text);
    }

    /**
     * Разбирает выражение и убеждается, что в строке не осталось лишних символов.
     *
     * @return дерево выражения
     */
    Expression parse() {
        Expression result = expression();

        skipSpaces();
        if (position != text.length()) {
            throw error("unexpected text");
        }

        return result;
    }

    /**
     * Читает одно выражение: число, переменную или операцию в скобках.
     *
     * @return распознанное поддерево
     */
    private Expression expression() {
        skipSpaces();

        if (take('(')) {
            Expression left = expression();
            skipSpaces();
            if (position >= text.length()) {
                throw error("expected an operator");
            }

            char op = text.charAt(position++);
            if (op != '+' && op != '-' && op != '*' && op != '/') {
                throw error("unknown operator: " + op);
            }

            Expression right = expression();
            if (!take(')')) {
                throw error("expected ')'");
            }

            switch (op) {
                case '+': return new Add(left, right);
                case '-': return new Sub(left, right);
                case '*': return new Mul(left, right);
                default: return new Div(left, right);
            }
        }

        int start = position;
        if (position < text.length() && text.charAt(position) == '-') {
            position++;
            if (position >= text.length() || !Character.isDigit(text.charAt(position))) {
                throw error("expected a number after '-'");
            }
        }

        if (position < text.length() && Character.isDigit(text.charAt(position))) {
            while (position < text.length() && Character.isDigit(text.charAt(position))) {
                position++;
            }
            try {
                return new Number(Integer.parseInt(
                        text.substring(start, position)));
            } catch (NumberFormatException ex) {
                throw error("integer is out of range");
            }
        }

        if (position < text.length()
                && (Character.isLetter(text.charAt(position)) || text.charAt(position) == '_')) {
            position++;
            while (position < text.length()
                    && (Character.isLetterOrDigit(text.charAt(position))
                    || text.charAt(position) == '_')) {
                position++;
            }
            return new Variable(text.substring(start, position));
        }

        throw error("expected a number, variable, or '('");
    }

    /**
     * Проверяет следующий символ и, если нашёл, переходит на него.
     * @param expected Оюидаемый символ.
     * @return Нашли ли символ.
     */
    private boolean take(char expected) {
        skipSpaces();

        if (position < text.length() && text.charAt(position) == expected) {
            position++;
            return true;
        }

        return false;
    }

    /**
     * Пропускает все пробелы до следующего символа.
     */
    private void skipSpaces() {
        while (position < text.length() && Character.isWhitespace(text.charAt(position))) {
            position++;
        }
    }

    /**
     * Создаёт ошибку.
     * @param message Текст ошибки.
     * @return Сама ошибка.
     */
    private IllegalArgumentException error(String message) {
        return new IllegalArgumentException(message + " at position " + position);
    }
}
