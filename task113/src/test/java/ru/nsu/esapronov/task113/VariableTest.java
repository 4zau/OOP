package ru.nsu.esapronov.task113;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Тесты класса Variable. */
class VariableTest {
    @Test
    void variableUsesAssignedValue() {
        Expression variable = new Variable("value_2");

        assertEquals("value_2", variable.toString());
        assertEquals(-7, variable.eval("value_2 = -7"));
        assertEquals(1, variable.derivative("value_2").eval(""));
        assertEquals(0, variable.derivative("x").eval(""));
    }

    @Test
    void invalidVariableNamesAreRejected() {
        String[] names = {"", "2x", "x-y", "x y", null};
        for (String name : names) {
            assertThrows(IllegalArgumentException.class, () -> new Variable(name), name);
        }
    }

    @Test
    void missingVariableValueIsRejected() {
        Expression expression = Expression.parse("(x+y)");

        assertThrows(IllegalArgumentException.class, () -> expression.eval("x=1"));
        assertThrows(IllegalArgumentException.class, () -> expression.eval(""));
    }
}
