package ru.nsu.esapronov.task112;

import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameUITest {
    @Test
    void testAskPlayerChoice() {
        GameUI ui = new GameUI(new Scanner("1\n"));

        assertEquals("1", ui.askPlayerChoice());
    }

    @Test
    void testAskNextRound() {
        GameUI ui = new GameUI(new Scanner("q\n"));

        assertEquals("q", ui.askNextRound());
    }
}

