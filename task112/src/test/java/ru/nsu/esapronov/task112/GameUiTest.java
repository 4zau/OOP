package ru.nsu.esapronov.task112;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Scanner;
import org.junit.jupiter.api.Test;

class GameUiTest {
    @Test
    void testAskPlayerChoice() {
        GameUi ui = new GameUi(new Scanner("1\n"));

        assertEquals("1", ui.askPlayerChoice());
    }

    @Test
    void testAskNextRound() {
        GameUi ui = new GameUi(new Scanner("q\n"));

        assertEquals("q", ui.askNextRound());
    }
}

