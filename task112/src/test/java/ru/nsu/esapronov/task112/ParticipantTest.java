package ru.nsu.esapronov.task112;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ParticipantTest {
    /**
    * Тест.
     */
    @Test
    public void testPlayerInitialization() {
        Player player = new Player();
        assertEquals("Игрок", player.getName());
        assertEquals(0, player.getScore());
    }

    /**
     * Тест.
     */
    @Test
    public void testParticipantBust() {
        Dealer dealer = new Dealer();
        dealer.addCard(new Card(Rank.TEN, Suit.SPADES));
        dealer.addCard(new Card(Rank.TEN, Suit.HEARTS));
        dealer.addCard(new Card(Rank.FIVE, Suit.CLUBS));

        assertTrue(dealer.isBusted());
        assertEquals(25, dealer.getScore());
    }

    /**
     * Тест.
     */
    @Test
    public void testClearHand() {
        Player player = new Player();
        player.addCard(new Card(Rank.TEN, Suit.SPADES));
        player.clearHand();

        assertEquals(0, player.getScore());
        assertFalse(player.isBusted());
    }
}