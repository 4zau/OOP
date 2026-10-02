package ru.nsu.esapronov.task113;

/** Общая основа операций с левым и правым операндами. */
abstract class BinaryExpression extends Expression {
    protected final Expression left;
    protected final Expression right;

    /**
     * Сохраняет операнды бинарной операции.
     *
     * @param left левый операнд
     * @param right правый операнд
     * @throws NullPointerException если операнд равен null
     */
    protected BinaryExpression(Expression left, Expression right) {
        this.left = java.util.Objects.requireNonNull(left);
        this.right = java.util.Objects.requireNonNull(right);
    }

    /**
     * Возвращает знак операции для строкового представления.
     *
     * @return символ операции
     */
    protected abstract char operator();

    /**
     * Записывает бинарную операцию вместе со скобками.
     *
     * @return строка вида (left+right)
     */
    @Override
    public final String toString() {
        return "(" + left + operator() + right + ")";
    }
}