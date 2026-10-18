package ru.nsu.esapronov.task113;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Тесты класса BinaryExpression. */
class BinaryExpressionTest {
    @Test
    void binaryOperationsRejectNullOperands() {
        assertThrows(NullPointerException.class, () -> new Add(null, new Number(1)));
        assertThrows(NullPointerException.class, () -> new Sub(new Number(1), null));
        assertThrows(NullPointerException.class, () -> new Mul(null, new Number(1)));
        assertThrows(NullPointerException.class, () -> new Div(new Number(1), null));
    }

    @Test
    void nestedExpressionHasExpectedTextAndValue() {
        Expression expression = new Add(new Number(3),
                new Mul(new Number(2), new Variable("x")));

        assertEquals("(3+(2*x))", expression.toString());
        assertEquals(23, expression.eval("x = 10"));
    }
}
