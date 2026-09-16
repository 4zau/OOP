package ru.nsu.esapronov.task112;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    @Test
    public void testDeckDraw() {
        Deck deck = new Deck(1);
        Card card = deck.draw();
        assertNotNull(card);
    }

    @Test
    public void testDeckReshuffle() {
        Deck deck = new Deck(1);
        for (int i = 0; i < 52; i++) {
            deck.draw();
        }

        Card extraCard = deck.draw();
        assertNotNull(extraCard);
    }
}