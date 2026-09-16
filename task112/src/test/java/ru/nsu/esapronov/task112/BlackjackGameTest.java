package ru.nsu.esapronov.task112;

import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class BlackjackGameTest {

    @Test
    public void testGameQuitImmediately() {
        Scanner mockScanner = new Scanner("0\nq\n");
        BlackjackGame game = new BlackjackGame(mockScanner);

        assertDoesNotThrow(game::start);
    }
}