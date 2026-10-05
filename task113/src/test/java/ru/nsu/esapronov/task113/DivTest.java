package ru.nsu.esapronov.task113;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Тесты класса Div. */
class DivTest {
    @Test
    void integerDivisionTruncatesTowardZero() {
        assertEquals(2, new Div(new Number(7), new Number(3)).eval(""));
        Expression expression = new Div(new Number(-7), new Number(3));
        assertEquals(-2, expression.eval(""));
        assertEquals("(-7/3)", expression.toString());
    }

    @Test
    void divisionByZeroIsRejected() {
        Expression expression = Expression.parse("(10/(x-2))");

        assertThrows(ArithmeticException.class, () -> expression.eval("x=2"));
    }
}
