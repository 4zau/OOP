package ru.nsu.esapronov.task112;

import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

