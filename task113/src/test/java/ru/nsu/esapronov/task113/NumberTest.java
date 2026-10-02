package ru.nsu.esapronov.task113;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Тесты класса Number. */
class NumberTest {
    @Test
    void numberStoresItsValue() {
        Expression number = new Number(-42);

        assertEquals(-42, number.eval(""));
        assertEquals("-42", number.toString());
        assertEquals(0, number.derivative("x").eval(""));
    }
}
